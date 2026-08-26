package com.onride.ride_service.redis;

import java.util.UUID;

public final class RedisKeys {

    private static final String QUOTE_PREFIX = "ride:quote:";

    private RedisKeys() {
    }

    public static String quote(UUID riderId) {
        return QUOTE_PREFIX + riderId;
    }
}