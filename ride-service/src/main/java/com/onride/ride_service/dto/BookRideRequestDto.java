package com.onride.ride_service.dto;

import com.onride.ride_service.enums.VehicleType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookRideRequestDto(

        @NotNull(message = "quoteId must not be null")
        UUID quoteId,

        @NotNull(message = "vehicleType must not be null")
        VehicleType vehicleType

) {
}