package com.onride.matching_service.dto;

import java.util.UUID;

public record PendingRideRequestDto(
        UUID rideId,
        UUID riderId,
        double pickupLat,
        double pickupLng,
        String pickupGeoCell,
        long requestedAt,
        long waitPenaltySeconds
) {
}