package com.onride.assistant_service.controller;

import com.onride.assistant_service.dto.ChatRequestDto;
import com.onride.assistant_service.dto.ChatResponseDto;
import com.onride.assistant_service.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/assistant")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public ChatResponseDto chat(
            @RequestHeader("X-User-Id") UUID riderId,
            @Valid @RequestBody ChatRequestDto request) {
        return chatService.chat(riderId, request);
    }
}