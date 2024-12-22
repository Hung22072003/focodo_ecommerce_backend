package focodo_ecommerce.backend.exception;

import focodo_ecommerce.backend.model.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse> handleUncategorizedException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.builder().code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()).message(e.getMessage()).build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity<ApiResponse> handleExpiredJwtException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.builder().code(ErrorCode.TOKEN_EXPIRED.getCode()).message(ErrorCode.TOKEN_EXPIRED.getMessage()).build());
    }
    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.badRequest().body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        return ResponseEntity.badRequest().body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }
}
