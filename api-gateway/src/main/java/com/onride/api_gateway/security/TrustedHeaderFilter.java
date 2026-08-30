package com.onride.api_gateway.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


public class TrustedHeaderFilter implements WebFilter {

    static final String USER_ID_HEADER = "X-User-Id";
    static final String USER_ROLE_HEADER = "X-User-Role";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerWebExchange cleaned = exchange.mutate()
                .request(request -> request.headers(headers -> {
                    headers.remove(USER_ID_HEADER);
                    headers.remove(USER_ROLE_HEADER);
                }))
                .build();

        return ReactiveSecurityContextHolder.getContext()
                .map(context -> withTrustedHeaders(cleaned, context.getAuthentication()))
                .defaultIfEmpty(cleaned)
                .flatMap(chain::filter);
    }

    private ServerWebExchange withTrustedHeaders(ServerWebExchange exchange, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return exchange;
        }

        String userId = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replace("ROLE_", ""))
                .orElse("");

        return exchange.mutate()
                .request(request -> request.headers(headers -> {
                    headers.set(USER_ID_HEADER, userId);
                    headers.set(USER_ROLE_HEADER, role);
                }))
                .build();
    }
}