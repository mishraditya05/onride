package com.onride.location_service.redis;

import com.onride.location_service.config.LocationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Repository
@RequiredArgsConstructor
public class DriverLocationStore {

    private final StringRedisTemplate redis;
    private final RedisScript<Long> pingScript;
    private final LocationProperties properties;
    
    public boolean savePosition(UUID driverId, double lat, double lng, String cellId, long timestampMillis) {
        Long changedCell = redis.execute(
                pingScript,
                List.of(RedisKeys.driver(driverId), RedisKeys.cell(cellId)),
                Double.toString(lat),
                Double.toString(lng),
                cellId,
                driverId.toString(),
                Long.toString(timestampMillis),
                Long.toString(properties.positionTtl().toSeconds()),
                RedisKeys.CELL_PREFIX
        );

        return changedCell != null && changedCell == 1L;
    }
    
    public Set<String> findFreshDriverIds(List<String> cellIds, long since) {
        if (cellIds.isEmpty()) {
            return Set.of();
        }

        List<Object> replies = redis.executePipelined(new SessionCallback<Object>() {
            @Override
            @SuppressWarnings("unchecked")
            public <K, V> Object execute(RedisOperations<K, V> operations) {
                RedisOperations<String, String> ops = (RedisOperations<String, String>) operations;

                for (String cellId : cellIds) {
                    ops.opsForZSet().rangeByScore(RedisKeys.cell(cellId), since, Double.POSITIVE_INFINITY);
                }
                for (String cellId : cellIds) {
                    ops.opsForZSet().removeRangeByScore(RedisKeys.cell(cellId), Double.NEGATIVE_INFINITY, since - 1);
                }

                return null;
            }
        });

        Set<String> driverIds = new LinkedHashSet<>();
        for (Object reply : replies.subList(0, Math.min(cellIds.size(), replies.size()))) {
            if (reply instanceof Collection<?> members) {
                members.forEach(member -> driverIds.add(member.toString()));
            }
        }

        return driverIds;
    }


    public List<DriverPosition> findPositions(Collection<String> driverIds) {
        if (driverIds.isEmpty()) {
            return List.of();
        }

        List<String> ordered = List.copyOf(driverIds);

        List<Object> replies = redis.executePipelined(new SessionCallback<Object>() {
            @Override
            @SuppressWarnings("unchecked")
            public <K, V> Object execute(RedisOperations<K, V> operations) {
                RedisOperations<String, String> ops = (RedisOperations<String, String>) operations;
                ordered.forEach(driverId -> ops.opsForHash().entries(RedisKeys.driver(driverId)));
                return null;
            }
        });

        List<DriverPosition> positions = new ArrayList<>(ordered.size());
        for (int i = 0; i < ordered.size() && i < replies.size(); i++) {
            DriverPosition position = toPosition(ordered.get(i), replies.get(i));
            if (position != null) {
                positions.add(position);
            }
        }

        return positions;
    }

    private DriverPosition toPosition(String driverId, Object reply) {
        if (!(reply instanceof Map<?, ?> fields) || fields.isEmpty()) {
            return null;
        }

        try {
            return new DriverPosition(
                    UUID.fromString(driverId),
                    Double.parseDouble(String.valueOf(fields.get("lat"))),
                    Double.parseDouble(String.valueOf(fields.get("lng"))),
                    Long.parseLong(String.valueOf(fields.get("ts")))
            );
        } catch (IllegalArgumentException | NullPointerException ex) {
            log.warn("Skipping driver {} with unreadable position {}", driverId, fields);
            return null;
        }
    }
}