package dutkercz.hi_backend.exceptions;

public class RateLimitExceededException extends RuntimeException {
    private final long waitForRefillSeconds;

    public RateLimitExceededException(long waitForRefillSeconds) {
        super("Muitas tentativas de recuperação de senha para este e-mail.");
        this.waitForRefillSeconds = waitForRefillSeconds;
    }

    public long getWaitForRefillSeconds() {
        return waitForRefillSeconds;
    }
}
