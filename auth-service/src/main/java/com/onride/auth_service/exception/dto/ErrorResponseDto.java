package com.onride.auth_service.exception.dto;

import com.onride.auth_service.exception.ErrorCode;

import java.time.Instant;

public record ErrorResponseDto(
        Instant timestamp,
        int status,
        ErrorCode error,
        String message,
        String path
) {
}
