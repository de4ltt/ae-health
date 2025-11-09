package feo.health.auth_service.exception;

import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CompletableFuture<ResponseEntity<ApiErrorResponse>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return CompletableFuture.completedFuture(
                ResponseEntity.badRequest()
                        .body(ApiErrorResponse.of(HttpStatus.BAD_REQUEST, message))
        );
    }

    @ExceptionHandler(AuthException.class)
    public CompletableFuture<ResponseEntity<ApiErrorResponse>> handleAuth(AuthException ex) {
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiErrorResponse.of(HttpStatus.UNAUTHORIZED, ex.getMessage()))
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public CompletableFuture<ResponseEntity<ApiErrorResponse>> handleRuntime(RuntimeException ex) {
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage()))
        );
    }

    @ExceptionHandler(Exception.class)
    public CompletableFuture<ResponseEntity<ApiErrorResponse>> handleOther(Exception ex) {
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"))
        );
    }
}