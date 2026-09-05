package com.onride.ride_service.client.dto;

import java.util.UUID;

public record MatchInfoDto(
        UUID matchId,
        UUID rideId,
        UUID riderId
) {
}