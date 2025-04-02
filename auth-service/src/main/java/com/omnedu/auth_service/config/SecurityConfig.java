package com.omnedu.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import com.omnedu.auth_service.security.AuthEntryPointJwt;
import com.omnedu.auth_service.security.AuthTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("unused")
    private final UserDetailsService userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    public SecurityConfig(UserDetailsService userDetailsService, AuthEntryPointJwt unauthorizedHandler, AuthTokenFilter authTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable()) // Disable CORS as it's handled by the gateway
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(unauthorizedHandler)) // Handle unauthorized requests
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll() // Allow access to authentication endpoints
                .anyRequest().authenticated()) // Require authentication for all other endpoints
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    // No need for CORS configuration source as it's handled by the gateway
}
