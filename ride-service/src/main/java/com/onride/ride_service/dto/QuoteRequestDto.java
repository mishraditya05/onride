package com.onride.ride_service.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record QuoteRequestDto(

        @NotNull(message = "pickupLat must not be null")
        @DecimalMin(value = "-90.0", message = "pickupLat must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "pickupLat must be between -90 and 90")
        Double pickupLat,

        @NotNull(message = "pickupLng must not be null")
        @DecimalMin(value = "-180.0", message = "pickupLng must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "pickupLng must be between -180 and 180")
        Double pickupLng,

        @NotNull(message = "dropLat must not be null")
        @DecimalMin(value = "-90.0", message = "dropLat must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "dropLat must be between -90 and 90")
        Double dropLat,

        @NotNull(message = "dropLng must not be null")
        @DecimalMin(value = "-180.0", message = "dropLng must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "dropLng must be between -180 and 180")
        Double dropLng

) {
}