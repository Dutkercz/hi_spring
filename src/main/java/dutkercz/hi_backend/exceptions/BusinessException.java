package dutkercz.hi_backend.exceptions;


public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
