package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.DailyPricesResponse;
import dutkercz.hi_backend.dto.stay.StayPayment;
import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.exceptions.BusinessException;
import dutkercz.hi_backend.exceptions.PaymentException;
import dutkercz.hi_backend.mapper.DailyPriceMapper;
import dutkercz.hi_backend.mapper.StayMapper;
import dutkercz.hi_backend.model.*;
import dutkercz.hi_backend.model.enums.RoomStatusEnum;
import dutkercz.hi_backend.model.enums.StayStatus;
import dutkercz.hi_backend.repository.DailyPriceRepository;
import dutkercz.hi_backend.repository.RoomRepository;
import dutkercz.hi_backend.repository.StayRepository;
import dutkercz.hi_backend.service.utils.HelperStayCalcs;
import dutkercz.hi_backend.service.validations.client.ClientValidation;
import dutkercz.hi_backend.service.validations.room.RoomValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class StayService {

    private final ClientValidation clientValidation;
    private final StayMapper stayMapper;
    private final RoomValidation roomValidation;
    private final StayRepository stayRepository;
    private final DailyPriceRepository dailyPriceRepository;
    private final DailyPriceMapper dailyPriceMapper;
    private final RoomRepository roomRepository;

    @Transactional
    public StayResponseDto newStay(StayRequestDto request) {
        log.info("StayRequestDto: {}", request);
        Client client = clientValidation.validateWithId(request.clientId());

        Room room = roomValidation.validateRoomInRequest(request);

        LocalDateTime checkInAjustado = HelperStayCalcs.adjustCheckin(request.checkIn());
        LocalDateTime checkOutAjustado = HelperStayCalcs.adjustCheckout(request.checkOut());

        BigDecimal stayDailyPrice = HelperStayCalcs.calcPerDayPrice(dailyPriceRepository.findAll(),
                                                                    request.totalGuests());

        long dailyRates = HelperStayCalcs.calcDailyRates(checkInAjustado, checkOutAjustado);

        BigDecimal totalPrice = stayDailyPrice.multiply(BigDecimal.valueOf(dailyRates));

        Stay stay = stayMapper.toEntity(request, client, room, checkInAjustado, checkOutAjustado, stayDailyPrice,
                                            totalPrice, dailyRates, StayStatus.CURRENT);
        if(stay.getIsPaid()){
            stay.setPaidPrice(totalPrice);
        }
        room.setStatus(RoomStatusEnum.OCCUPIED);

        stayRepository.save(stay);
        for (StayGuest sg : request.stayGuests()){
            stay.addStayGuest(sg);
        }
        log.info("New stay has been created: {}", stay);
        return stayMapper.toResponse(stay);
    }


    @Transactional
    public void addStay(Long id) {
        var stay = stayRepository.findById(id).orElseThrow(() ->
                                            new EntityNotFoundException("Stay with id " + id + " not found"));
        stay.setCheckOut(stay.getCheckOut().plusDays(1));
        long dailyRates = HelperStayCalcs.calcDailyRates(stay.getCheckIn(), stay.getCheckOut());
        stay.setDailyRates(dailyRates);
        stay.setIsPaid(false);
        stay.setTotalPrice(stay.getDailyPrice().multiply(BigDecimal.valueOf(dailyRates)));
    }

    @Transactional
    public StayResponseDto addPayment(Long id, StayPayment paymentRequest) {
        var stay = stayRepository.findById(id).orElseThrow(() ->
                                           new EntityNotFoundException("Stay with id " + id + " not found"));
        if (paymentRequest.amount().compareTo(stay.getTotalPrice()) > 0) {
            throw new PaymentException("This payment amount exceeds daily total amount");
        }
        if (paymentRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentException("Payment amount cant be negative or zero");
        }

        var payment = stayMapper.toPaymentEntity(paymentRequest);
        stay.addPaymentAmount(payment);
        return stayMapper.toResponse(stay);
    }

    public DailyPricesResponse dailyPrices() {
        DailyPrices last = dailyPriceRepository.findAll().getLast();
        return dailyPriceMapper.toResponse(last);
    }

    @Transactional
    public void checkout(Long id) {
        Stay stay = stayRepository.findById(id).orElseThrow(() ->
                                new EntityNotFoundException("Stay with id " + id + " not found"));
        Room room = stay.getRoom();

        long actualDailyRates = HelperStayCalcs.calcDailyRates(stay.getCheckIn(),
                                           HelperStayCalcs.adjustCheckout(LocalDateTime.now()));
        if (stay.getDailyRates() != actualDailyRates){
            throw new BusinessException("Total daily rates do not match");
        }

        if (!stay.getIsPaid() || stay.getPaidPrice().compareTo(stay.getTotalPrice()) != 0) {
            throw new PaymentException("Stay is not paid");
        }

        stay.setCheckOut(HelperStayCalcs.adjustCheckout(stay.getCheckOut()));
        stay.setStayStatus(StayStatus.FINISHED);
        room.setStatus(RoomStatusEnum.AVAILABLE);
    }

    @Transactional
    public StayResponseDto updateDailyRates(Long id) {
        var stay = stayRepository.findById(id).orElseThrow(() ->
                                                   new EntityNotFoundException("Stay with id " + id + " not found"));
        var actualCheckout =  HelperStayCalcs.adjustCheckout(LocalDateTime.now());
        stay.setCheckOut(actualCheckout);
        long actualDailyRates = HelperStayCalcs.calcDailyRates(stay.getCheckIn(), actualCheckout);
        stay.setDailyRates(actualDailyRates);
        stay.setTotalPrice(stay.getDailyPrice().multiply(BigDecimal.valueOf(actualDailyRates)));
        return stayMapper.toResponse(stay);
    }
}

