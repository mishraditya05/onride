package com.onride.matching_service.dto;

import com.onride.matching_service.enums.MatchingMode;

import java.util.UUID;

public record PendingRideRequestDto(
        UUID rideId,
        UUID riderId,
        double pickupLat,
        double pickupLng,
        String pickupGeoCell,
        long requestedAt,
        long waitPenaltySeconds,
        MatchingMode matchingMode
) {
}
