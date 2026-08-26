package com.onride.ride_service.dto;

import com.onride.ride_service.enums.VehicleType;

import java.util.Map;
import java.util.UUID;

public record QuoteResponseDto(
        UUID quoteId,
        String currency,
        long distanceMetres,
        Map<VehicleType, VehicleQuoteDto> vehicle
) {
}