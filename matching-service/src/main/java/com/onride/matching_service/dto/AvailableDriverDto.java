package com.onride.matching_service.dto;

import java.util.UUID;

public record AvailableDriverDto(
        UUID driverId,
        double lat,
        double lng,
        long lastSeenAt
) {
}