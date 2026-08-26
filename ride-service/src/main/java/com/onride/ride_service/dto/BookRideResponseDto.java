package com.onride.ride_service.dto;

import com.onride.ride_service.enums.RideStatus;
import com.onride.ride_service.enums.VehicleType;

import java.math.BigDecimal;
import java.util.UUID;

public record BookRideResponseDto(
        UUID rideId,
        RideStatus status,
        VehicleType vehicleType,
        long distanceMetres,
        BigDecimal fare,
        String currency
) {
}