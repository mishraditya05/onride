package com.onride.matching_service.redis;

public final class RedisKeys {

    private static final String PENDING_PREFIX = "matching:pending:";

    private RedisKeys() {
    }

    public static String pending(String cellId) {
        return PENDING_PREFIX + cellId;
    }

    public static String pendingPattern() {
        return PENDING_PREFIX + "*";
    }

    public static String cellIdOf(String pendingKey) {
        return pendingKey.substring(PENDING_PREFIX.length());
    }
}