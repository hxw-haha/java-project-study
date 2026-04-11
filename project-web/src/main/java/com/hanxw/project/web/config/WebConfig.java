package com.hanxw.project.web.config;

import com.hanxw.project.common.web.interceptor.AuthInterceptor;
import com.hanxw.project.common.redis.limiter.RateLimiterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 把临时 PDF 目录映射为 /temp/pdf/ 访问路径
        registry.addResourceHandler("/temp/pdf/**")
                .addResourceLocations("file:" + System.getProperty("java.io.tmpdir") + "/pdf-reports/");
        // 映射 /temp/pdf/**  到 项目根目录/temp/pdf/
        // 使用绝对路径（强烈推荐）
        String tempPath = System.getProperty("user.dir") + "/temp/pdf/";
        registry.addResourceHandler("/temp/pdf/**")
                .addResourceLocations("file:" + tempPath);
    }
}
