package com.omnedu.module_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.Key;
import java.util.Base64;
import java.util.Enumeration;

@Component
@Slf4j
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Log all headers for debugging
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("Header: {} = {}", headerName, request.getHeader(headerName));
        }

        // Skip for OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        try {
            // Get token from Authorization header
            String authHeader = request.getHeader("Authorization");
            log.info("Authorization Header: {}", authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            String token = authHeader.substring(7);
            log.info("Extracted Token: {}", token);

            Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));

            // Validate token using the new parserBuilder() approach
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract user ID from token and add to request
            String userId = claims.getSubject();
            log.info("Extracted User ID: {}", userId);
            request.setAttribute("X-User-Id", userId);

            return true;
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}