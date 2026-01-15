package com.hanxw.project.config;

import com.hanxw.project.common.interceptor.AuthInterceptor;
import com.hanxw.project.common.redis.limiter.RateLimiterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private RateLimiterInterceptor rateLimiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //限流
        registry.addInterceptor(rateLimiterInterceptor)
                .addPathPatterns("/**")
                .order(0);

        //鉴权
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/health",
                        "/error"
                )
                .order(1);
    }
}
