package dutkercz.hi_backend.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DailyPricesUpdateDto(
        @NotNull(message = "O identificador é obrigatório")
        Long id,

        @Positive(message = "O valor da diária deve ser maior que zero")
        @Digits(integer = 4, fraction = 2, message = "O valor da diária deve ter no máximo 4 dígitos inteiros e 2 decimais")
        BigDecimal oneGuestPrice,

        @Positive(message = "O valor da diária deve ser maior que zero")
        @Digits(integer = 4, fraction = 2, message = "O valor da diária deve ter no máximo 4 dígitos inteiros e 2 decimais")
        BigDecimal twoGuestPrice,

        @Positive(message = "O valor da diária deve ser maior que zero")
        @Digits(integer = 4, fraction = 2, message = "O valor da diária deve ter no máximo 4 dígitos inteiros e 2 decimais")
        BigDecimal threeGuestPrice,

        @Positive(message = "O valor da diária deve ser maior que zero")
        @Digits(integer = 4, fraction = 2, message = "O valor da diária deve ter no máximo 4 dígitos inteiros e 2 decimais")
        BigDecimal fourGuestPrice
) {
}
