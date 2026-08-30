package com.onride.api_gateway.security;

import com.onride.common.web.jwt.InvalidJwtException;
import com.onride.common.web.jwt.JwtTokenProvider;
import com.onride.common.web.jwt.JwtUserPrincipal;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;


public class JwtAuthenticationFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return chain.filter(exchange);
        }

        String token = header.substring(BEARER_PREFIX.length());

        try {
            JwtUserPrincipal principal = jwtTokenProvider.parse(token);

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + principal.role()));
            var authentication = new UsernamePasswordAuthenticationToken(principal.userId(), null, authorities);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(
                            Mono.just(new SecurityContextImpl(authentication))));
        } catch (InvalidJwtException e) {
            return chain.filter(exchange);
        }
    }
}