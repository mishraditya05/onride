package com.onride.assistant_service.controller;

import com.onride.assistant_service.dto.ChatRequestDto;
import com.onride.assistant_service.dto.ChatResponseDto;
import com.onride.assistant_service.dto.ChatSessionDto;
import com.onride.assistant_service.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/assistant/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/sessions")
    public ChatSessionDto create(@RequestHeader("X-User-Id") UUID riderId) {
        return chatService.createSession(riderId);
    }

    @PostMapping("/sessions/{sessionId}/messages")
    public ChatResponseDto sendMessage(
            @RequestHeader("X-User-Id") UUID riderId,
            @PathVariable UUID sessionId,
            @Valid @RequestBody ChatRequestDto request) {
        return chatService.sendMessage(riderId, sessionId, request);
    }

    @PostMapping("/sessions/{sessionId}/close")
    public ChatSessionDto close(
            @RequestHeader("X-User-Id") UUID riderId,
            @PathVariable UUID sessionId) {
        return chatService.closeSession(riderId, sessionId);
    }
}