package com.onride.ride_service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Quote> findAndDelete(UUID riderId) {
        String json = redis.opsForValue().getAndDelete(RedisKeys.quote(riderId));
        if (json == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, Quote.class));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to deserialize quote for rider " + riderId, ex);
        }
    }
}