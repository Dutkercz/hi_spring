package dutkercz.hi_backend.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DailyPricesUpdateDto(
        @NotNull
        Long id,

        @Positive(message = "O valor deve ser maior que 0")
        @Digits(integer = 4, fraction = 2, message = "O campo deve ter no máximo 4 dígitos inteiros")
        BigDecimal oneGuestPrice,

        @Positive(message = "O valor deve ser maior que 0")
        @Digits(integer = 4, fraction = 2, message = "O campo deve ter no máximo 4 dígitos inteiros")
        BigDecimal twoGuestPrice,

        @Positive(message = "O valor deve ser maior que 0")
        @Digits(integer = 4, fraction = 2, message = "O campo deve ter no máximo 4 dígitos inteiros")
        BigDecimal threeGuestPrice,

        @Positive(message = "O valor deve ser maior que 0")
        @Digits(integer = 4, fraction = 2, message = "O campo deve ter no máximo 4 dígitos inteiros")
        BigDecimal fourGuestPrice
) {
}
