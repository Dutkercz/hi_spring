package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.mapper.StayMapper;
import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.StayGuest;
import dutkercz.hi_backend.model.enums.RoomStatusEnum;
import dutkercz.hi_backend.model.enums.StayStatus;
import dutkercz.hi_backend.repository.RoomRepository;
import dutkercz.hi_backend.repository.StayRepository;
import dutkercz.hi_backend.service.utils.HelperStayCalcs;
import dutkercz.hi_backend.service.validations.client.ClientValidation;
import dutkercz.hi_backend.service.validations.room.RoomValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class StayService {

    private final ClientValidation clientValidation;
    private final StayMapper stayMapper;
    private final RoomValidation roomValidation;
    private final StayRepository stayRepository;
    private final RoomRepository roomRepository;


    @Transactional
    public StayResponseDto newStay(StayRequestDto request) {
        log.info("StayRequestDto: {}", request);
        Client client = clientValidation.validateWithId(request.clientId());
        Room room = roomRepository.findById(request.roomId()).orElseThrow(() ->
                new EntityNotFoundException("Room with id " + request.roomId() + " not found"));
        // 1. Aplica as regras de ajuste de horários de entrada e saída
        LocalDateTime checkInAjustado = adjustCheckin(request.checkIn());
        LocalDateTime checkOutAjustado = adjustCheckout(request.checkOut());
        BigDecimal stayDailyPrice = HelperStayCalcs.calcPerDayPrice(request.totalGuests());

        // 2. Calcula a quantidade de diárias com base nos horários corrigidos
        long dailyRates = HelperStayCalcs.calcDailyRates(checkInAjustado, checkOutAjustado);

        // 3. Calcula o valor total financeiro
        BigDecimal valorTotal = stayDailyPrice.multiply(BigDecimal.valueOf(dailyRates));

        Stay stay = stayMapper.toEntity(request, client, room, checkInAjustado, checkOutAjustado, stayDailyPrice,
                                        valorTotal, StayStatus.CURRENT);
        room.setStatus(RoomStatusEnum.OCCUPIED);

        stayRepository.save(stay);
        for (StayGuest sg : request.stayGuests()){
            stay.addStayGuest(sg);
        }
        log.info("New stay has been created: {}", stay);
        return stayMapper.toResponse(stay);
    }

    /**
     *CHECKIN: Se chegar entre meia-noite (00:00) e o horário limite, considera-se que a diária iniciou às 13:01 do
     * dia anterior.
     */
    private LocalDateTime adjustCheckin(LocalDateTime checkInOriginal) {
        LocalTime checkinOriginalHour = checkInOriginal.toLocalTime(); //exp : 4h
        LocalTime maxHourToCheckin = LocalTime.of(7, 0); //depois desse horario não hospedar até 12h

        // Define uma janela de tolerância na madrugada (ex: chegou entre 00:00 e 7:00)
        if (checkinOriginalHour.isBefore(maxHourToCheckin)) {
            //jogamos o checkIn para o dia anterior, as 14h
            return checkInOriginal.minusDays(1).with(LocalTime.of(14, 0));
        }
        return checkInOriginal;
    }

    /**
     * CHECKOUT: Se passar das 13:00 (já com 1h de 'luz') adicionamos uma diária cheia cobrando mais um dia.
     **/
    private LocalDateTime adjustCheckout(LocalDateTime checkOutOriginal) {
        LocalTime checkoutHourOriginal = checkOutOriginal.toLocalTime(); //exp 11h
        LocalTime checkoutLimit = LocalTime.of(13, 0);

        // Se o checkout for após as 13:00, jogamos a data para o dia seguinte às 12h
        // para garantir que o calculo de dias adicione 1 diária a mais
        if (checkoutHourOriginal.isAfter(checkoutLimit)) {
            return checkOutOriginal.plusDays(1).with(LocalTime.of(12, 0));
        }
        return checkOutOriginal;
    }


    @Transactional
    public void addStay(Long id) {
        var stay = stayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Stay with id " + id + " not found"));
        stay.setCheckOut(stay.getCheckOut().plusDays(1));
        long dailyRates = HelperStayCalcs.calcDailyRates(stay.getCheckIn(), stay.getCheckOut());
        stay.setTotalPrice(stay.getDailyPrice().multiply(BigDecimal.valueOf(dailyRates)));
    }
}

