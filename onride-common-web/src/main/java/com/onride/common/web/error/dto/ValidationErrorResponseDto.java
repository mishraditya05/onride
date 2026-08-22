package com.onride.common.web.error.dto;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors
) {

    public record FieldError(String field, String message) {
    }
}