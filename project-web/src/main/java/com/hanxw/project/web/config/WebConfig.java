package com.hanxw.project.web.config;

import com.hanxw.project.common.interceptor.AuthInterceptor;
import com.hanxw.project.common.redis.limiter.RateLimiterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置
 * 配置拦截器、跨域等
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private RateLimiterInterceptor rateLimiterInterceptor;
    
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 限流拦截器（优先级最高）
        registry.addInterceptor(rateLimiterInterceptor)
                .addPathPatterns("/**")
                .order(0);

        // 登录认证拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",           // 登录接口
                        "/health",          // 健康检查
                        "/error"            // 错误页面
                )
                .order(1);
    }
}
