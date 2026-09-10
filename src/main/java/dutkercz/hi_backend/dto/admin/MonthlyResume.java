package dutkercz.hi_backend.dto.admin;

import java.math.BigDecimal;

public record MonthlyResume(
        BigDecimal totalMonthProfit,
        BigDecimal percentageChange,
        BigDecimal lastMonthProfit,
        Long actualMonthDailyRates,
        Long lastMonthDailyRates,
        BigDecimal percentageActualMonthlyOccupation,
        BigDecimal percentageLastMonthlyOccupation) {
}
