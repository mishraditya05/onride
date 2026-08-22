package com.onride.location_service.redis;

import java.util.UUID;


public record DriverPosition(
        UUID driverId,
        double lat,
        double lng,
        long timestamp
) {
}