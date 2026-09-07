package com.clario.app.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(int status, String message, LocalDateTime timestamp, Map<String, String> fieldErrors) {
    public ApiError(int status, String message) {
        this(status, message, LocalDateTime.now(), null);
    }
    public ApiError(int status, String message, Map<String, String> fieldErrors) {
        this(status, message, LocalDateTime.now(), fieldErrors);
    }
}
