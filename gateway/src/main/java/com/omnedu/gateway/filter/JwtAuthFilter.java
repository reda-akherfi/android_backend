package com.omnedu.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;
import java.util.Base64;
import java.util.List;

// @Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    static {
        System.out.println("JwtAuthFilter class is being loaded!");
    }

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final Key signingKey;

    public JwtAuthFilter() {
        super(Config.class);
        log.info("Initializing JwtAuthFilter");
        this.signingKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(
                        "sVRs1xcOZIMjT1ArjTkpfurGNxgeTJV2uJXU1iEB0LrW2fB5k0kfKDG0/AR66ksVpmwHzll4+c4fYw+TuuU67A=="));
    }

    

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            
            log.info("Request path: {}", path);
            log.info("Request method: {}", request.getMethod());
            
            // Log all headers for debugging
            HttpHeaders headers = request.getHeaders();
            headers.forEach((key, value) -> 
                log.info("Header - {}: {}", key, value)
            );

            // Skip JWT check for public endpoints
            if (path.startsWith("/api/auth/login") || 
                path.startsWith("/api/auth/register") || 
                path.startsWith("/api/auth/verify")  ||
                path.startsWith("/api/notes")
                ) {
                log.info("Public endpoint detected, bypassing authentication");
                return chain.filter(exchange);
            }

            // Check for Authorization header
            List<String> authHeaders = headers.get(HttpHeaders.AUTHORIZATION);
            if (authHeaders == null || authHeaders.isEmpty()) {
                log.warn("No Authorization header found");
                return unauthorized(exchange, "Missing Authorization header");
            }

            String authHeader = authHeaders.get(0);
            log.info("Authorization header: {}", authHeader);

            if (!authHeader.startsWith("Bearer ")) {
                log.warn("Invalid Authorization header format");
                return unauthorized(exchange, "Invalid Authorization header format");
            }

            // Extract token
            String token = authHeader.substring(7);
            log.info("Extracted token length: {}", token.length());

            try {
                // Validate token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(signingKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                log.info("Token validated successfully for subject: {}", claims.getSubject());

                // Add headers for downstream services
                ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(r -> r.header("X-User-Id", claims.getSubject()))
                    .build();

                return chain.filter(modifiedExchange);

            } catch (Exception e) {
                log.error("Token validation failed", e);
                return unauthorized(exchange, "Invalid token: " + e.getMessage());
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        log.warn("Unauthorized access: {}", message);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {
        // Configuration properties if needed
    }
}