package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.DailyPricesDto;
import dutkercz.hi_backend.dto.DailyPricesUpdateDto;
import dutkercz.hi_backend.dto.admin.MonthlyResume;
import dutkercz.hi_backend.exceptions.ResourceNotFoundException;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.repository.DailyPriceRepository;
import dutkercz.hi_backend.repository.RoomRepository;
import dutkercz.hi_backend.repository.StayRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final DailyPriceRepository dailyPriceRepository;
    private final StayRepository stayRepository;
    private final RoomRepository roomRepository;

    public DailyPricesDto getDailyPrices() {
        return new DailyPricesDto(dailyPriceRepository.findAll().getLast());
    }

    @Transactional
    public DailyPricesDto adjustDailyPrice(@Valid DailyPricesUpdateDto request) throws BadRequestException {
        var dailyPrices = dailyPriceRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Not found daily prices"));
        verifyNumbersBeforeUpdate(request);
        dailyPrices.setOneGuestPrice(request.oneGuestPrice());
        dailyPrices.setTwoGuestPrice(request.twoGuestPrice());
        dailyPrices.setThreeGuestPrice(request.threeGuestPrice());
        dailyPrices.setFourGuestPrice(request.fourGuestPrice());
        return new DailyPricesDto(dailyPrices);
    }

    private void verifyNumbersBeforeUpdate(@Valid DailyPricesUpdateDto request) throws BadRequestException {
        if (request.oneGuestPrice().compareTo(BigDecimal.ZERO) < 0 ||
                request.twoGuestPrice().compareTo(BigDecimal.ZERO) < 0 ||
                request.threeGuestPrice().compareTo(BigDecimal.ZERO) < 0 ||
                request.fourGuestPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("O valor deve ser maior que zero");
        }
    }

    public MonthlyResume totalStaysAmountPerMonth(int year, int month) {
        var actualMonthDate = LocalDate.of(year, month, 1);
        var lastMonthDate = LocalDate.of(year, month, 1).minusMonths(1);
        var firstDay = actualMonthDate.atTime(12, 1, 0);
        var lastDay = actualMonthDate.with(TemporalAdjusters.lastDayOfMonth())
                .plusDays(1)
                .atTime(11, 59, 0);

        var actualMonthStays = stayRepository.findAllByCheckOutBetween(firstDay, lastDay);
        var lastMonthStays = getLatMonthStays(actualMonthDate);

        var actualMonthProfit = getTotalStaysAmountPerMonth(actualMonthStays);
        var lastMonthProfit = getTotalStaysAmountPerMonth(lastMonthStays);
        var percentageProfitChange = calculatePercentageChange(actualMonthProfit, lastMonthProfit);

        var actualMonthDailyRates = getTotalDailyRates(actualMonthStays);
        var lastMonthDailyRates = getTotalDailyRates(lastMonthStays);

        var percentageActualMonthlyOccupation = calculatePercentageMonthlyOccupation(actualMonthDailyRates,
                actualMonthDate);
        var percentageLastMonthlyOccupation = calculatePercentageMonthlyOccupation(lastMonthDailyRates,
                lastMonthDate);

        return new MonthlyResume(actualMonthProfit, percentageProfitChange,
                lastMonthProfit, actualMonthDailyRates, lastMonthDailyRates,
                percentageActualMonthlyOccupation, percentageLastMonthlyOccupation);
    }

    private BigDecimal calculatePercentageMonthlyOccupation(Long totalMonthOccupation, LocalDate initDate) {
        var availableMonthOccupation = roomRepository.findAll().size() * initDate.lengthOfMonth();
        if (availableMonthOccupation == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.valueOf(totalMonthOccupation)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(availableMonthOccupation), 2, RoundingMode.HALF_UP);
    }

    /// Calculo da variação de porcentagem
    /// valorAtual - valorAnterior / valorAnterior = totalParcial
    /// total = totalParcial * 100
    private BigDecimal calculatePercentageChange(BigDecimal actual, BigDecimal last) {
        if (last.compareTo(BigDecimal.ZERO) <= 0 ||
                actual.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return actual.subtract(last)
                .divide(last, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getTotalStaysAmountPerMonth(List<Stay> stays) {
        return stays.stream().map(Stay::getPaidPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<Stay> getLatMonthStays(LocalDate initDate) {
        var lastMonthDate = initDate.minusMonths(1);

        var firstDay = lastMonthDate.atTime(12, 1, 0);
        var lastDay = lastMonthDate.with(TemporalAdjusters.lastDayOfMonth())
                .atTime(11, 59, 0);
        return stayRepository.findAllByCheckOutBetween(firstDay, lastDay);
    }

    /// Calculo da quantidade de diárias baseado na lista de stays
    private Long getTotalDailyRates(List<Stay> stays) {
        return stays.stream().mapToLong(Stay::getDailyRates).sum();
    }
}
