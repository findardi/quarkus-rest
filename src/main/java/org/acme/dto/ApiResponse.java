package org.acme.dto;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public record ApiResponse<T>(
    int status,
    String message,
    T data,
    OffsetDateTime timestamp
) {
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(
            status,
            message,
            data,
            OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return success(201, message, data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return success(200, message, data);
    }
}
