package dutkercz.hi_backend.dto;

import java.math.BigDecimal;

public record DailyPricesDto(
        BigDecimal oneGuestPrice,
        BigDecimal twoGuestPrice,
        BigDecimal threeGuestPrice,
        BigDecimal fourGuestPrice
) {
}
