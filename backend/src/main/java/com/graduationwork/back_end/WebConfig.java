package com.graduationwork.back_end;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configure CORS to allow requests from specific origins
        registry.addMapping("/**")  // Apply CORS to all endpoints
                .allowedOrigins("http://localhost:8080", "http://127.0.0.1:5000") // Allow requests from Spring Boot and Flask servers
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true);  // Allow credentials (cookies, authorization headers, etc.)

        registry.addMapping("/**")  // 모든 경로에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000")  // 클라이언트 앱의 URL (예: React 앱이 실행되는 주소)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                .allowedHeaders("*");  // 모든 헤더를 허용

    }
}
