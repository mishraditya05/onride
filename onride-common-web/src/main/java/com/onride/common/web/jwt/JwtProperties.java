package com.onride.common.web.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "onride.jwt")
public record JwtProperties(String secret, long expirationSeconds) {
}