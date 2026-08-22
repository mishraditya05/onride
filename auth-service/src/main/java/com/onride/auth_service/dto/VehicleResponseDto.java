package com.onride.auth_service.dto;

import com.onride.auth_service.enums.VehicleStatus;
import com.onride.auth_service.enums.VehicleType;

import java.util.UUID;

public record VehicleResponseDto(
        UUID id,
        UUID driverId,
        String brand,
        String model,
        Integer year,
        String color,
        String licensePlate,
        VehicleType vehicleType,
        VehicleStatus status
) {
}