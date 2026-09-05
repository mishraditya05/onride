package com.onride.matching_service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onride.matching_service.config.MatchingProperties;
import com.onride.matching_service.dto.MatchedRideDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MatchedRideRequestStore {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final MatchingProperties properties;

    public boolean tryLock(UUID driverId, MatchedRideDto match) {
        Boolean locked = redis.opsForValue().setIfAbsent(
                RedisKeys.matched(driverId.toString()),
                serialize(match),
                Duration.ofSeconds(properties.offerLockTtlSeconds()));

        return Boolean.TRUE.equals(locked);
    }

    private String serialize(MatchedRideDto match) {
        try {
            return objectMapper.writeValueAsString(match);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize matched ride " + match.rideId(), ex);
        }
    }
}