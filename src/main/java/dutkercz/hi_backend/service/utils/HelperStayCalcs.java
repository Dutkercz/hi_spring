package dutkercz.hi_backend.service.utils;

import dutkercz.hi_backend.model.DailyPrices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class HelperStayCalcs {


    //calcula o valor de 1 diária baseado no número de pagantes
    public static BigDecimal calcPerDayPrice(List<DailyPrices> dailyPricesList, int numberOfGuests) {
        var dailyPrices = dailyPricesList.getLast();
        var rate =  dailyPrices.getFourGuestPrice().divide(BigDecimal.valueOf(4),
                                                           RoundingMode.HALF_UP);
        return switch (numberOfGuests) {
            case 1 -> dailyPrices.getOneGuestPrice();
            case 2 -> dailyPrices.getTwoGuestPrice();
            case 3 -> dailyPrices.getThreeGuestPrice();
            case 4 -> dailyPrices.getFourGuestPrice();
            default -> rate.multiply(BigDecimal.valueOf(numberOfGuests));
        };
    }

    /**
     * Calcula quantos blocos de 24 horas (diárias) existem entre o check-in e check-out ajustados.
     * Garante o mínimo de 1 diária caso o tempo seja muito curto.
     */
    //calcula o numero de diárias, baseado no checkin e checkout, seguindo as regras do hotel
    public static long calcDailyRates(LocalDateTime checkIn, LocalDateTime checkOut) {
        long dias = Duration
                .between(checkIn.truncatedTo(ChronoUnit.DAYS), checkOut.truncatedTo(ChronoUnit.DAYS))
                .toDays();
        log.info("Daily Rates: {}", checkIn);
        log.info("Daily Rates: {}", checkOut);
        log.info("Daily Rates: {}", dias);
        return Math.max(1, dias);
    }

    /**
     * CHECKOUT: Se passar das 13:00 (já com 1h de 'luz') adicionamos uma diária cheia cobrando mais um dia.
     **/
    public static LocalDateTime adjustCheckout(LocalDateTime checkOutOriginal) {
        LocalTime checkoutHourOriginal = checkOutOriginal.toLocalTime(); //exp 11h
        LocalTime checkoutLimit = LocalTime.of(13, 0);

        // Se o checkout for após as 13:00, jogamos a data para o dia seguinte às 12h
        // para garantir que o calculo de dias adicione 1 diária a mais
        if (checkoutHourOriginal.isAfter(checkoutLimit)) {
            return checkOutOriginal.plusDays(1).with(LocalTime.of(12, 0));
        }
        return checkOutOriginal;
    }

    /**
     *CHECKIN: Se chegar entre meia-noite (00:00) e o horário limite, considera-se que a diária iniciou às 13:01 do
     * dia anterior.
     */
    public static LocalDateTime adjustCheckin(LocalDateTime checkInOriginal) {
        LocalTime checkinOriginalHour = checkInOriginal.toLocalTime(); //exp : 4h
        LocalTime maxHourToCheckin = LocalTime.of(7, 0); //depois desse horario não hospedar até 12h

        // Define uma janela de tolerância na madrugada (ex: chegou entre 00:00 e 7:00)
        if (checkinOriginalHour.isBefore(maxHourToCheckin)) {
            //jogamos o checkIn para o dia anterior, as 14h
            return checkInOriginal.minusDays(1).with(LocalTime.of(14, 0));
        }
        return checkInOriginal;
    }
}
