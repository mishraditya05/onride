package com.onride.location_service.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "onride.location")
public record LocationProperties(

        @Min(value = 7, message = "h3Resolution must be between 7 and 12")
        @Max(value = 12, message = "h3Resolution must be between 7 and 12")
        int h3Resolution,

        @NotNull(message = "positionTtl must be set")
        Duration positionTtl

) {
}