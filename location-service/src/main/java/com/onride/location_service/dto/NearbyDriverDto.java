package com.onride.location_service.dto;

import java.util.UUID;

public record NearbyDriverDto(
        UUID driverId,
        double lat,
        double lng,
        long distanceMetres,
        long lastSeenAt
) {
}