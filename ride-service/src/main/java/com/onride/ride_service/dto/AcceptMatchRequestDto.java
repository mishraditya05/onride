package com.onride.ride_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AcceptMatchRequestDto(

        @NotNull(message = "matchId must not be null")
        UUID matchId

) {
}