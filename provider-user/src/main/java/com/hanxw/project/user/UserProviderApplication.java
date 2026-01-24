package com.hanxw.project.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户服务提供者启动类
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.hanxw.project.user.mapper")
public class UserProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class, args);
        System.out.println("====================================");
        System.out.println("  User Provider Started (Port:8081) ");
        System.out.println("====================================");
    }
}
