package com.onride.ride_service.client.dto;

import java.util.UUID;

public record NearbyDriverDto(
        UUID driverId,
        double lat,
        double lng,
        long distanceMetres,
        long lastSeenAt
) {
}