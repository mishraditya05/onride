package com.onride.assistant_service.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "onride.assistant")
public record AssistantProperties(
        @NotNull(message = "chatTtl must be set")
        Duration chatTtl
) {
}
