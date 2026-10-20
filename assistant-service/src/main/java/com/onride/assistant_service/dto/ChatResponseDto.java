package com.onride.assistant_service.dto;

import com.onride.assistant_service.enums.ConversationPhase;

import java.util.UUID;

public record ChatResponseDto(
        UUID sessionId,
        String reply,
        ConversationPhase phase
) {
}