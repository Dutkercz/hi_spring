package dutkercz.hi_backend.dto.admin;

import java.math.BigDecimal;

public record MonthlyResume(
        BigDecimal totalMonthProfit,
        Double percentageChange,
        BigDecimal lastMonthProfit,
        Long actualMonthDailyRates,
        Long lastMonthDailyRates,
        BigDecimal percentageActualMonthlyOccupation,
        BigDecimal percentageLastMonthlyOccupation) {
}
