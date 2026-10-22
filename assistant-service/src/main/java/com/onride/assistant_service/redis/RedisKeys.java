package com.onride.assistant_service.redis;

public final class RedisKeys {

    private static final String CHAT_PREFIX = "assistant:chat:";

    private RedisKeys() {
    }

    public static String chat(String conversationId) {
        return CHAT_PREFIX + conversationId;
    }

    public static String chatPattern() {
        return CHAT_PREFIX + "*";
    }

    public static String conversationIdOf(String chatKey) {
        return chatKey.substring(CHAT_PREFIX.length());
    }
}
