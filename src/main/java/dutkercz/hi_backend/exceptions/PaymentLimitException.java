package dutkercz.hi_backend.exceptions;

public class PaymentLimitException extends RuntimeException {
    public PaymentLimitException(String message) {
        super(message);
    }
}
