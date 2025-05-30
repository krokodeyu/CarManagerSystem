package database.cms.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        HttpStatus status = mapErrorCodeToStatus(ex.getErrorCode());
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(AuthErrorException.class)
    public ResponseEntity<ErrorResponse> handleAuthErrorException(AuthErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        HttpStatus status = mapErrorCodeToStatus(ex.getErrorCode());
        return ResponseEntity.status(status).body(errorResponse);
    }

    private HttpStatus mapErrorCodeToStatus(String errorCode) {
        return switch (errorCode) {
            case "INVALID_LOGIN_INFO" -> HttpStatus.UNAUTHORIZED; // 401
            case "USER_NOT_FOUND", "VEHICLE_NOT_FOUND", "TECH_NOT_FOUND", "ORDER_NOT_FOUND", "APPOINTMENT_NOT_FOUND"-> HttpStatus.NOT_FOUND;         // 404
            default -> HttpStatus.BAD_REQUEST;                    // 400
        };
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
        log.error("Internal server error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
