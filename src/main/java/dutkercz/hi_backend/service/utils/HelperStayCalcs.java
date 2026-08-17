package dutkercz.hi_backend.service.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
public class HelperStayCalcs {

    public static BigDecimal calcPerDayPrice(int numberOfGuests) {
        return switch (numberOfGuests) {
            case 1 -> new BigDecimal("160.0");
            case 2 -> new BigDecimal("260.0");
            case 3 -> new BigDecimal("340.0");
            case 4 -> new BigDecimal("490.0");
            default -> new BigDecimal("120.0").multiply(BigDecimal.valueOf(numberOfGuests));
        };
    }

    /**
     * Calcula quantos blocos de 24 horas (diárias) existem entre o check-in e check-out ajustados.
     * Garante o mínimo de 1 diária caso o tempo seja muito curto.
     */
    public static long calcDailyRates(LocalDateTime checkIn, LocalDateTime checkOut) {
        long dias = Duration
                .between(checkIn.truncatedTo(ChronoUnit.DAYS), checkOut.truncatedTo(ChronoUnit.DAYS))
                .toDays();
        log.info("Daily Rates: {}", checkIn);
        log.info("Daily Rates: {}", checkOut);
        log.info("Daily Rates: {}", dias);
        return Math.max(1, dias);
    }
}
