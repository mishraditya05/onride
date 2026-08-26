package com.onride.ride_service.config;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "onride.pricing")
public record PricingProperties(
        @DecimalMin(value = "1.0", message = "roadFactor must be at least 1.0")
        double roadFactor,

        @Min(value = 1, message = "averageSpeedKmph must be at least 1")
        int averageSpeedKmph,

        @NotNull(message = "quoteTtl must be set")
        Duration quoteTtl

) {
}