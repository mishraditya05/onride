package com.onride.api_gateway.config;

import com.onride.api_gateway.security.JwtAuthenticationFilter;
import com.onride.api_gateway.security.RestAccessDeniedHandler;
import com.onride.api_gateway.security.RestAuthenticationEntryPoint;
import com.onride.api_gateway.security.TrustedHeaderFilter;
import com.onride.common.web.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtTokenProvider jwtTokenProvider,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/auth/signup", "/api/v1/auth/login").permitAll()
                        .pathMatchers("/api/v1/auth/users/**").hasRole("ADMIN")
                        .pathMatchers("/api/v1/rides/quotes", "/api/v1/rides/book").hasRole("RIDER")
                        .pathMatchers("/api/v1/rides/matches", "/api/v1/rides/*/accept").hasRole("DRIVER")
                        .pathMatchers("/api/v1/riders/**").hasRole("RIDER")
                        .pathMatchers("/api/v1/drivers/**", "/api/v1/locations/ping").hasRole("DRIVER")
                        .anyExchange().authenticated())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterAt(new JwtAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(new TrustedHeaderFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .build();
    }
}
