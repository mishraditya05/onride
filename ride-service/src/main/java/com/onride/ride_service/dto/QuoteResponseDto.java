package com.onride.ride_service.dto;

import com.onride.ride_service.enums.VehicleType;

import java.util.Map;

public record QuoteResponseDto(
        String currency,
        long distanceMetres,
        Map<VehicleType, VehicleQuoteDto> vehicle
) {
}