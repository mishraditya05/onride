package com.onride.auth_service.exception.dto;

import com.onride.auth_service.exception.ErrorCode;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponseDto(
        Instant timestamp,
        int status,
        ErrorCode error,
        String message,
        String path,
        List<FieldError> fieldErrors
) {

    public record FieldError(String field, String message) {
    }
}
