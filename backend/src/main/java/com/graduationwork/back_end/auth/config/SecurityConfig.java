package com.graduationwork.back_end.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/user/register",
                                "/api/user/login",
                                "/api/user/logout",
                                "/predict",
                                "/receipt/**",  // ✅ `/receipt/upload` 허용
                                "/item/**", // ✅ `/item/save` 허용
                                "/api/recommend/**",
                                "/recommend/**"

                        ).permitAll()
                        .anyRequest().authenticated() // 나머지는 인증 필요
                );
        return http.build();
    }
}
