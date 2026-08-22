package com.onride.location_service.redis;

import java.util.UUID;

public final class RedisKeys {

    public static final String CELL_PREFIX = "cell:";
    private static final String DRIVER_PREFIX = "driver:";

    private RedisKeys() {
    }

    public static String driver(UUID driverId) {
        return DRIVER_PREFIX + driverId;
    }

    public static String cell(String cellId) {
        return CELL_PREFIX + cellId;
    }
}