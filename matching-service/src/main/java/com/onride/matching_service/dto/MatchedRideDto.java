package com.onride.matching_service.dto;

import java.util.UUID;

public record MatchedRideDto(
        UUID matchId,
        UUID rideId,
        UUID riderId
) {
}