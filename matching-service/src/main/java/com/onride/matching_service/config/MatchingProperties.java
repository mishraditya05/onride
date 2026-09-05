package com.onride.matching_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "onride.matching")
public record MatchingProperties(
        int averageSpeedKmph,
        long batchIntervalMs,
        long offerLockTtlSeconds
) {
}