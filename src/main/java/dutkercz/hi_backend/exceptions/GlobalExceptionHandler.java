package dutkercz.hi_backend.exceptions;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CepNotExistException.class, ResourceNotFoundException.class})
    public ResponseEntity<ProblemDetail> handleCepNotExistException(RuntimeException e) {
        var error = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({RoomBadRequestException.class, InvalidOtpCode.class, BusinessException.class,
                       PaymentException.class})
    public ResponseEntity<ProblemDetail> handleRoomBadRequest(RuntimeException e) {
        var error = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ProblemDetail> handleResourceExistsException(ResourceExistsException e) {
        var error = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(WeakKeyException.class)
    public ResponseEntity<ProblemDetail> handleJwtException(RuntimeException e) {
        var error = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ProblemDetail>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ProblemDetail> errors = new ArrayList<>();
        e.getFieldErrors().forEach(error -> {
            errors.add(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, error.getDefaultMessage()));
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({CookieInvalidException.class, JwtException.class})
    public ResponseEntity<ProblemDetail> handleCookieInvalidException(CookieInvalidException e) {
        var error = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        var error = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationDeniedException() {
        var error = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
                                                     "Você não tem permissão para isso");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ProblemDetail> handleRateLimit(RateLimitExceededException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
             .header("Retry-After", String.valueOf(e.getWaitForRefillSeconds()))
             .body(ProblemDetail.forStatusAndDetail(HttpStatus.TOO_MANY_REQUESTS,
    "Muitas tentativas para este email, tente novamente em " + e.getWaitForRefillSeconds() + " minutos"));
    }
}
