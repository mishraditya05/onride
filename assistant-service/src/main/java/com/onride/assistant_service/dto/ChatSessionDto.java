package com.onride.assistant_service.dto;

import com.onride.assistant_service.enums.SessionStatus;

import java.util.UUID;

public record ChatSessionDto(
        UUID sessionId,
        SessionStatus status
) {
}