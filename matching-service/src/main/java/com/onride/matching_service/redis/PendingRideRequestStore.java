package com.onride.matching_service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onride.matching_service.dto.PendingRideRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PendingRideRequestStore {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    public void save(PendingRideRequestDto request) {
        redis.opsForList().rightPush(RedisKeys.pending(request.pickupGeoCell()), serialize(request));
    }

    public Map<String, List<PendingRideRequestDto>> findAndDeleteAll() {
        Set<String> cellKeys = redis.keys(RedisKeys.pendingPattern());
        if (cellKeys == null || cellKeys.isEmpty()) {
            return Map.of();
        }

        Map<String, List<PendingRideRequestDto>> byCell = new LinkedHashMap<>();
        for (String cellKey : cellKeys) {
            List<PendingRideRequestDto> requests = findAndDelete(cellKey);
            if (!requests.isEmpty()) {
                byCell.put(RedisKeys.cellIdOf(cellKey), requests);
            }
        }

        return byCell;
    }

    private List<PendingRideRequestDto> findAndDelete(String cellKey) {
        List<String> raw = redis.opsForList().range(cellKey, 0, -1);
        redis.delete(cellKey);

        if (raw == null || raw.isEmpty()) {
            return List.of();
        }

        return raw.stream().map(this::deserialize).toList();
    }

    private String serialize(PendingRideRequestDto request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize pending ride request " + request.rideId(), ex);
        }
    }

    private PendingRideRequestDto deserialize(String json) {
        try {
            return objectMapper.readValue(json, PendingRideRequestDto.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to deserialize pending ride request " + json, ex);
        }
    }
}