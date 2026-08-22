package com.onride.location_service.redis;

import com.onride.location_service.config.LocationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


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
}