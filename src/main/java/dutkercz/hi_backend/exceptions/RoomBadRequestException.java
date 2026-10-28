package dutkercz.hi_backend.exceptions;

public class RoomBadRequestException extends RuntimeException {
    public RoomBadRequestException(String message) {
        super(message);
    }
}
