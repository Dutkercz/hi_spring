package dutkercz.hi_backend.exceptions;

public class CepNotExistException extends RuntimeException {
    public CepNotExistException(String message) {
        super(message);
    }
}
