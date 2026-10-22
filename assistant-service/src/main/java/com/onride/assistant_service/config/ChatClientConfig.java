package com.onride.assistant_service.config;

import com.onride.assistant_service.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    private static final String SYSTEM_PROMPT = """
            You are the OnRide booking assistant. You help a rider book a cab, and nothing else.

            Your job is to collect three things, one at a time, in a natural conversation:
            the pickup location, the drop location, and any ride preferences the rider
            mentions, such as cab size or needing space for luggage.

            Rules you must never break:
            - Never state a fare, a distance, an arrival time, or a driver's name.
              You cannot see any of those yet. If the rider asks, say you will have the
              price once you know both locations.
            - Never claim a ride is booked. You cannot book anything yet.
            - If the rider asks about something that is not booking a cab, say that you
              only handle ride booking and offer to help with that instead.

            Keep replies to one or two short sentences. Ask for one missing detail at a time.
            """;

    @Bean
    public ChatMemory chatMemory(RedisChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}