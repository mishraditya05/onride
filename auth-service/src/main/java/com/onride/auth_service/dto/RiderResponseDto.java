package com.onride.auth_service.dto;

import com.onride.auth_service.enums.RiderOnboardingStage;

import java.time.LocalDate;
import java.util.UUID;

public record RiderResponseDto(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        RiderOnboardingStage onboardingStage
) {
}