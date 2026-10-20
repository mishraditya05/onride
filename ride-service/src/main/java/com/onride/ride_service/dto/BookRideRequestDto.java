package com.onride.ride_service.dto;

import com.onride.ride_service.enums.MatchingMode;
import com.onride.ride_service.enums.VehicleType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record BookRideRequestDto(

        @NotNull(message = "quoteId must not be null")
        UUID quoteId,

        @NotNull(message = "vehicleType must not be null")
        VehicleType vehicleType,

        MatchingMode matchingMode,

        @Size(max = 255, message = "broadcastMessage must not exceed 255 characters")
        String broadcastMessage,

        @Size(max = 255, message = "riderNote must not exceed 255 characters")
        String riderNote

) {
}
