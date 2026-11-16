package dutkercz.hi_backend.dto.stay;

import dutkercz.hi_backend.model.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StayPayment(
        @NotNull(message = "A forma de pagamento é obrigatória")
        PaymentMethod method,

        @NotNull(message = "O valor do pagamento é obrigatório")
        @DecimalMin(value = "0", inclusive = false, message = "O valor do pagamento deve ser maior que zero")
        BigDecimal amount) {
}
