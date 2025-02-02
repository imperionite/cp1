package com.imperionite.cp1.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS for all endpoints
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow desired methods
            .allowedOrigins("*"); // Allow any origin (modify as needed)
    }

}