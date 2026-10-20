package com.onride.assistant_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChatRequestDto(

        UUID sessionId,

        @NotBlank(message = "message must not be blank")
        @Size(max = 1000, message = "message must be at most 1000 characters")
        String message

) {
}