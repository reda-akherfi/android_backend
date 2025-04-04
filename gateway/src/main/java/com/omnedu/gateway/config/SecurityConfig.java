package com.omnedu.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/api/auth/register", "/api/auth/login", "/api/auth/verify").permitAll()
                .pathMatchers("/actuator/**").permitAll()  // Allow actuator routes without authentication
                .pathMatchers("/api/notes/**").permitAll()  // Require authentication for notes service
                .pathMatchers("/api/videos/**").permitAll()  // Require authentication for videos service
                .pathMatchers("/api/documents/**").permitAll()  // Require authentication for document service
                .pathMatchers("/api/timer/**").permitAll()  // Require authentication for timer service
                .pathMatchers("/api/tasks/**").permitAll()  // Require authentication for tasks service
                .pathMatchers("/api/modules/**").permitAll()  // Require authentication for modules service
                .anyExchange().authenticated()
            );
        return http.build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}