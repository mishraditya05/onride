package com.onride.matching_service.redis;

public final class RedisKeys {

    private static final String PENDING_RIDES_PREFIX = "matching:pending-rides:";
    private static final String MATCHED_RIDES_PREFIX = "matching:matched-rides:";

    private RedisKeys() {
    }

    public static String pending(String cellId) {
        return PENDING_RIDES_PREFIX + cellId;
    }

    public static String pendingPattern() {
        return PENDING_RIDES_PREFIX + "*";
    }

    public static String cellIdOf(String pendingKey) {
        return pendingKey.substring(PENDING_RIDES_PREFIX.length());
    }

    public static String matched(String driverId) {
        return MATCHED_RIDES_PREFIX + driverId;
    }
}