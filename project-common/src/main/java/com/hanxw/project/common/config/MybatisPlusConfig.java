package com.hanxw.project.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 * 
 * 注意：@MapperScan 需要在各模块的启动类上配置，因为各模块的 mapper 包路径不同
 * - provider-user: @MapperScan("com.hanxw.project.user.mapper")
 * - provider-order: @MapperScan("com.hanxw.project.order.mapper")
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatis-Plus 核心拦截器（分页插件）
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件（必配）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
