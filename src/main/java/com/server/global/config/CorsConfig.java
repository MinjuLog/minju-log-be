package com.server.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

    private final IpBlockingInterceptor ipBlockingInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // 모든 URL 허용
                .allowedOrigins("*")        // 모든 도메인 허용
                .allowedMethods("*")        // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
                .allowedHeaders("*")        // 모든 헤더 허용
                .allowCredentials(false);   // 크리덴셜 필요 없으면 false, 필요하면 true + allowedOrigins에 "*" 사용 X
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipBlockingInterceptor)
                .addPathPatterns("/api/proposals/**");
    }
}
