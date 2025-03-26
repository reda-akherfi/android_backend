package com.omnedu.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Base64;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final Key signingKey;

    public JwtAuthenticationFilter() {
        this.signingKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(
                        "sVRs1xcOZIMjT1ArjTkpfurGNxgeTJV2uJXU1iEB0LrW2fB5k0kfKDG0/AR66ksVpmwHzll4+c4fYw+TuuU67A=="));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Log the request path and method for debugging
        String path = exchange.getRequest().getPath().toString();
        logger.info("JwtAuthenticationFilter triggered for path: {}", path);
        String method = exchange.getRequest().getMethod().toString();
        logger.info("Incoming request - Path: {}, Method: {}", path, method);

        // Skip authentication for public endpoints
        if (path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/register") ||
                path.startsWith("/api/auth/verify")) {
            logger.info("Public endpoint detected, bypassing authentication");
            return chain.filter(exchange);
        }

        // Log all headers for notes service routes
        HttpHeaders headers = exchange.getRequest().getHeaders();
        headers.forEach((key, value) -> logger.info("Header - {}: {}", key, value));

        // Check for Authorization header
        List<String> authHeaders = headers.get(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null || authHeaders.isEmpty()) {
            logger.warn("No Authorization header found for path: {}", path);
            return unauthorized(exchange, "Missing Authorization header");
        }

        String authHeader = authHeaders.get(0);
        logger.info("Authorization header: {}", authHeader);

        // Validate Authorization header format
        if (!authHeader.startsWith("Bearer ")) {
            logger.warn("Invalid Authorization header format for path: {}", path);
            return unauthorized(exchange, "Invalid Authorization header format");
        }

        // Extract and validate token
        try {
            String token = authHeader.substring(7);
            logger.info("Token length: {}", token.length());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            logger.info("Token validated successfully for subject: {}", claims.getSubject());
            logger.info("Forwarding request with headers: {}", exchange.getRequest().getHeaders());


            // Add user ID to request headers
            return chain.filter(
                    exchange.mutate()
                            .request(r -> r.header(HttpHeaders.AUTHORIZATION, authHeader))
                            .request(r -> r.header("X-User-Id", claims.getSubject()))
                            .build());

        } catch (Exception e) {
            logger.error("Token validation failed for path: {}", path, e);
            return unauthorized(exchange, "Invalid token: " + e.getMessage());
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        logger.warn("Unauthorized access: {}", message);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        // Ensure this filter runs early in the filter chain
        return -100;
    }
}