package com.onride.ride_service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class QuoteStore {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    public void save(Quote quote, Duration ttl) {
        try {
            String json = objectMapper.writeValueAsString(quote);
            redis.opsForValue().set(RedisKeys.quote(quote.riderId()), json, ttl);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize quote " + quote.quoteId(), ex);
        }
    }
}