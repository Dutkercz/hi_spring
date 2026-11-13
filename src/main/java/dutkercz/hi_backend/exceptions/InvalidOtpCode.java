package dutkercz.hi_backend.exceptions;

public class InvalidOtpCode extends RuntimeException {
    public InvalidOtpCode(String message) {
        super(message);
    }
}
