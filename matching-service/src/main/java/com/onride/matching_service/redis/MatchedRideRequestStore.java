package com.onride.matching_service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onride.matching_service.dto.MatchedRideDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MatchedRideRequestStore {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final RedisScript<String> confirmMatchScript;

    public boolean tryLock(UUID driverId, MatchedRideDto match) {
        Boolean locked = redis.opsForValue().setIfAbsent(
                RedisKeys.matched(driverId.toString()),
                serialize(match));

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

    public Set<UUID> findLockedDriverIds() {
        Set<String> keys = redis.keys(RedisKeys.matchedPattern());
        if (keys == null || keys.isEmpty()) {
            return Set.of();
        }

        return keys.stream()
                .map(RedisKeys::driverIdOf)
                .map(UUID::fromString)
                .collect(Collectors.toSet());
    }

    public List<MatchedRideDto> findAndDeleteExpired(long now) {
        Set<String> keys = redis.keys(RedisKeys.matchedPattern());
        if (keys == null || keys.isEmpty()) {
            return List.of();
        }

        List<MatchedRideDto> expired = new ArrayList<>();
        for (String key : keys) {
            String json = redis.opsForValue().get(key);
            if (json == null) {
                continue;
            }

            MatchedRideDto match = deserialize(json);
            if (match.expiresAt() <= now) {
                redis.delete(key);
                expired.add(match);
            }
        }

        return expired;
    }

    private String serialize(MatchedRideDto match) {
        try {
            return objectMapper.writeValueAsString(match);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize matched ride " + match.rider().rideId(), ex);
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