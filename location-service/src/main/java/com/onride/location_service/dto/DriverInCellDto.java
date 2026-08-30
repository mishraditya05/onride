package com.onride.location_service.dto;

import java.util.UUID;

public record DriverInCellDto(
        UUID driverId,
        double lat,
        double lng,
        long lastSeenAt
) {
}