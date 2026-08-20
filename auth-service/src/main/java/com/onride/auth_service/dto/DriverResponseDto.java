package com.onride.auth_service.dto;

import com.onride.auth_service.enums.DriverOnboardingStage;

import java.time.LocalDate;
import java.util.UUID;

public record DriverResponseDto(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        DriverOnboardingStage onboardingStage
) {
}