package com.onride.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVehicleRequestDto(

        @NotBlank(message = "brand must not be blank")
        String brand,

        @NotBlank(message = "model must not be blank")
        String model,

        @NotNull(message = "year must not be null")
        Integer year,

        @NotBlank(message = "color must not be blank")
        String color,

        @NotBlank(message = "licensePlate must not be blank")
        String licensePlate

) {
}