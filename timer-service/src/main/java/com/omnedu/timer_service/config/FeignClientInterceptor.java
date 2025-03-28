package com.omnedu.timer_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest; 


@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // Forward both X-User-Id AND Authorization header
            String userId = request.getHeader("X-User-Id");
            String authToken = request.getHeader("Authorization");
            
            if (userId != null) {
                template.header("X-User-Id", userId);
            }
            if (authToken != null) {
                template.header("Authorization", authToken);
            }
        }
    }
}