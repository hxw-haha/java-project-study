package com.hanxw.project.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Web 网关启动类（Dubbo 消费者）
 * 
 * 基于原有 Controller 改造
 */
@SpringBootApplication
@EnableDubbo
@ComponentScan(basePackages = {
        "com.hanxw.project"   // 扫描整个项目根包
})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
        System.out.println("====================================");
        System.out.println("  Web Gateway Started (Port:8080)   ");
        System.out.println("  http://localhost:8080             ");
        System.out.println("====================================");
    }
}
