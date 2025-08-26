package feo.health.auth_service.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {

    public static ApiErrorResponse of(HttpStatus status, String message) {
        return new ApiErrorResponse(status.value(), message, LocalDateTime.now());
    }
}
