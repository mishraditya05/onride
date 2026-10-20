package com.onride.matching_service.config;

import com.onride.matching_service.enums.MatchingMode;
import com.onride.matching_service.strategy.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MatchingStrategyConfig {

    @Bean
    public Map<MatchingMode, MatchingStrategy> matchingStrategies(List<MatchingStrategy> strategies) {
        return strategies.stream()
                .collect(Collectors.toUnmodifiableMap(MatchingStrategy::mode, Function.identity()));
    }
}
