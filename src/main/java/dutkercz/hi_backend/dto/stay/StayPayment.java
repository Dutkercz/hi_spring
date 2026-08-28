package dutkercz.hi_backend.dto.stay;

import dutkercz.hi_backend.model.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StayPayment(
        @NotNull(message = "Payment method cant be null")
        PaymentMethod method,

        @NotNull
        @DecimalMin(value = "0", message = "Payment amount cant be zero or negative")
        BigDecimal amount) {
}
