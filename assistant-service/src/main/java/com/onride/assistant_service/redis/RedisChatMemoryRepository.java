package com.onride.assistant_service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onride.assistant_service.config.AssistantProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring AI ships {@code ChatMemoryRepository} implementations for JDBC, Cassandra and
 * Neo4j, but not Redis, so this is that missing piece. Only USER and ASSISTANT messages
 * ever reach here while the assistant has no tools (Phase 2) — SYSTEM and TOOL message
 * round-tripping is added when a phase that produces them lands.
 */
@Repository
@RequiredArgsConstructor
public class RedisChatMemoryRepository implements ChatMemoryRepository {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final AssistantProperties properties;

    @Override
    public List<String> findConversationIds() {
        Set<String> keys = redis.keys(RedisKeys.chatPattern());
        if (keys == null || keys.isEmpty()) {
            return List.of();
        }
        return keys.stream().map(RedisKeys::conversationIdOf).toList();
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        String json = redis.opsForValue().get(RedisKeys.chat(conversationId));
        if (json == null) {
            return List.of();
        }
        return deserialize(json).stream().map(RedisChatMemoryRepository::toMessage).toList();
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        List<StoredMessage> stored = messages.stream().map(RedisChatMemoryRepository::toStored).toList();
        redis.opsForValue().set(RedisKeys.chat(conversationId), serialize(stored), properties.chatTtl());
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        redis.delete(RedisKeys.chat(conversationId));
    }

    private static StoredMessage toStored(Message message) {
        MessageType type = message.getMessageType();
        if (type != MessageType.USER && type != MessageType.ASSISTANT) {
            throw new IllegalArgumentException("Unsupported message type for chat memory: " + type);
        }
        return new StoredMessage(type, message.getText());
    }

    private static Message toMessage(StoredMessage stored) {
        return switch (stored.type()) {
            case USER -> new UserMessage(stored.text());
            case ASSISTANT -> new AssistantMessage(stored.text());
            case SYSTEM, TOOL -> throw new IllegalStateException(
                    "Unsupported stored message type: " + stored.type());
        };
    }

    private String serialize(List<StoredMessage> messages) {
        try {
            return objectMapper.writeValueAsString(messages);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize chat messages", ex);
        }
    }

    private List<StoredMessage> deserialize(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<StoredMessage>>() {
            });
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to deserialize chat messages", ex);
        }
    }

    private record StoredMessage(MessageType type, String text) {
    }
}
