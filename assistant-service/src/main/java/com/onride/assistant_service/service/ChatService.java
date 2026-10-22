package com.onride.assistant_service.service;

import com.onride.assistant_service.dto.ChatRequestDto;
import com.onride.assistant_service.dto.ChatResponseDto;
import com.onride.assistant_service.dto.ChatSessionDto;
import com.onride.assistant_service.entity.ChatSession;
import com.onride.assistant_service.enums.ConversationPhase;
import com.onride.assistant_service.enums.SessionStatus;
import com.onride.assistant_service.exception.ChatSessionClosedException;
import com.onride.assistant_service.repository.ChatSessionRepository;
import com.onride.common.web.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final ChatSessionRepository chatSessionRepository;

    @Transactional
    public ChatSessionDto createSession(UUID riderId) {
        ChatSession session = new ChatSession();
        session.setRiderId(riderId);
        session.setStatus(SessionStatus.ACTIVE);
        chatSessionRepository.save(session);

        log.info("chat session created rider={} session={}", riderId, session.getId());

        return new ChatSessionDto(session.getId(), session.getStatus());
    }

    @Transactional
    public ChatResponseDto sendMessage(UUID riderId, UUID sessionId, ChatRequestDto request) {
        ChatSession session = findOwnedSession(riderId, sessionId);

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new ChatSessionClosedException("Chat session is closed");
        }

        log.info("chat turn rider={} session={}", riderId, sessionId);

        String reply = chatClient.prompt()
                .user(request.message())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId.toString()))
                .call()
                .content();

        return new ChatResponseDto(sessionId, reply, ConversationPhase.COLLECTING_PICKUP);
    }

    @Transactional
    public ChatSessionDto closeSession(UUID riderId, UUID sessionId) {
        ChatSession session = findOwnedSession(riderId, sessionId);

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new ChatSessionClosedException("Chat session is already closed");
        }

        session.setStatus(SessionStatus.CLOSED);
        session.setClosedAt(Instant.now());
        chatSessionRepository.save(session);

        return new ChatSessionDto(session.getId(), session.getStatus());
    }

    private ChatSession findOwnedSession(UUID riderId, UUID sessionId) {
        return chatSessionRepository.findById(sessionId)
                .filter(s -> s.getRiderId().equals(riderId))
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));
    }
}