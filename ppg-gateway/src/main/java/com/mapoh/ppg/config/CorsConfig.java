package com.mapoh.ppg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mabohv
 * @date 2024/12/29 12:38
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // 前端地址
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
