package com.onride.ride_service.dto;

import com.onride.ride_service.enums.RideStatus;

import java.util.UUID;

public record CancelRideResponseDto(
        UUID rideId,
        RideStatus status
) {
}
