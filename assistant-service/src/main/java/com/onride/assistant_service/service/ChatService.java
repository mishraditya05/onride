package com.onride.assistant_service.service;

import com.onride.assistant_service.dto.ChatRequestDto;
import com.onride.assistant_service.dto.ChatResponseDto;
import com.onride.assistant_service.enums.ConversationPhase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    public ChatResponseDto chat(UUID riderId, ChatRequestDto request) {
        UUID sessionId = request.sessionId() != null ? request.sessionId() : UUID.randomUUID();

        log.info("chat turn rider={} session={}", riderId, sessionId);

        String reply = chatClient.prompt()
                .user(request.message())
                .call()
                .content();

        return new ChatResponseDto(
                sessionId,
                reply,
                ConversationPhase.COLLECTING_PICKUP);
    }
}