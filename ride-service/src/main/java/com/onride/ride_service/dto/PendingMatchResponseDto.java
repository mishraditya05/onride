package com.onride.ride_service.dto;

import java.util.UUID;

public record PendingMatchResponseDto(
        boolean found,
        UUID matchId,
        UUID rideId,
        UUID riderId
) {
}