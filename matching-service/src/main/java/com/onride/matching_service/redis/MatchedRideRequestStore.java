package com.onride.matching_service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onride.matching_service.config.MatchingProperties;
import com.onride.matching_service.dto.MatchedRideDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MatchedRideRequestStore {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final MatchingProperties properties;
    private final RedisScript<String> confirmMatchScript;

    public boolean tryLock(UUID driverId, MatchedRideDto match) {
        Boolean locked = redis.opsForValue().setIfAbsent(
                RedisKeys.matched(driverId.toString()),
                serialize(match),
                Duration.ofSeconds(properties.offerLockTtlSeconds()));

        return Boolean.TRUE.equals(locked);
    }

    public Optional<MatchedRideDto> find(UUID driverId) {
        String json = redis.opsForValue().get(RedisKeys.matched(driverId.toString()));
        return json == null ? Optional.empty() : Optional.of(deserialize(json));
    }

    public Optional<MatchedRideDto> tryConfirm(UUID driverId, UUID matchId) {
        String json = redis.execute(
                confirmMatchScript,
                List.of(RedisKeys.matched(driverId.toString())),
                matchId.toString());

        return json == null ? Optional.empty() : Optional.of(deserialize(json));
    }

    private String serialize(MatchedRideDto match) {
        try {
            return objectMapper.writeValueAsString(match);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize matched ride " + match.rideId(), ex);
        }
    }

    private MatchedRideDto deserialize(String json) {
        try {
            return objectMapper.readValue(json, MatchedRideDto.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to deserialize matched ride " + json, ex);
        }
    }
}