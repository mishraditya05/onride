package com.onride.matching_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class RedisScriptConfig {

    @Bean
    public RedisScript<String> confirmMatchScript() {
        return RedisScript.of(new ClassPathResource("scripts/confirm-match.lua"), String.class);
    }
}