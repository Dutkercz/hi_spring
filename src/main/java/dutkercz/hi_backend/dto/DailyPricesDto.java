package dutkercz.hi_backend.dto;

import dutkercz.hi_backend.model.DailyPrices;

import java.math.BigDecimal;

public record DailyPricesDto(
        BigDecimal oneGuestPrice,
        BigDecimal twoGuestPrice,
        BigDecimal threeGuestPrice,
        BigDecimal fourGuestPrice
) {
    public DailyPricesDto(DailyPrices x) {
        this(x.getOneGuestPrice(), x.getTwoGuestPrice(), x.getThreeGuestPrice(), x.getFourGuestPrice());
    }
}
