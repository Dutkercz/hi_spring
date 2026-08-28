package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.DailyPricesDto;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.repository.DailyPriceRepository;
import dutkercz.hi_backend.repository.StayRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final DailyPriceRepository dailyPriceRepository;
    private final StayRepository stayRepository;

    @Transactional
    public void adjustDailyPrice(@Valid DailyPricesDto request) {
        var dailyPrices = dailyPriceRepository.findAll().getLast();

        dailyPrices.setOneGuestPrice(request.oneGuestPrice());
        dailyPrices.setTwoGuestPrice(request.twoGuestPrice());
        dailyPrices.setThreeGuestPrice(request.threeGuestPrice());
        dailyPrices.setFourGuestPrice(request.fourGuestPrice());
    }

    public BigDecimal totalStaysAmountPerMonth(int year, int month) {
        var initDate = LocalDate.of( year, month, 1 );
        var firstDay = initDate.atTime(12, 1, 0);
        var lastDay = initDate.with(TemporalAdjusters.lastDayOfMonth())
                              .plusDays(1)
                              .atTime(11 , 59, 0);
        var stays = stayRepository.findAllByCheckOutBetween(firstDay, lastDay);

        BigDecimal total = stays.stream().map(Stay::getPaidPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("Total stays amount: {}", total);
        return total;
    }

}
