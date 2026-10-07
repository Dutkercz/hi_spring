package dutkercz.hi_backend.exceptions;

public class CookieInvalidException extends RuntimeException {
    public CookieInvalidException(String message) {
        super(message);
    }
}
