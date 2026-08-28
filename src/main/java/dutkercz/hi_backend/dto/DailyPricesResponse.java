package dutkercz.hi_backend.dto;

import java.math.BigDecimal;

public record DailyPricesResponse(
        BigDecimal oneGuestPrice,
        BigDecimal twoGuestPrice,
        BigDecimal threeGuestPrice,
        BigDecimal fourGuestPrice
) {
}
