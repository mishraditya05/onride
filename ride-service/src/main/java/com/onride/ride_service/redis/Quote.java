package com.onride.ride_service.redis;

import com.onride.ride_service.enums.VehicleType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record Quote(
        UUID quoteId,
        UUID riderId,
        double pickupLat,
        double pickupLng,
        double dropLat,
        double dropLng,
        String currency,
        long distanceMetres,
        Map<VehicleType, BigDecimal> fares
) {
}