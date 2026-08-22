package com.onride.location_service.dto;

public record LocationPingResponseDto(
        String cell,
        long recordedAt,
        boolean changedCell
) {
}