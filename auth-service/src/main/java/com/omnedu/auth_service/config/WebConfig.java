package com.omnedu.auth_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Gateway is handling CORS, so we don't need to configure it here
    // to avoid duplicate Access-Control-Allow-Origin headers
}