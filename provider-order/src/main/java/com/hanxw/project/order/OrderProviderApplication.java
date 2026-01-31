package com.hanxw.project.order;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 订单服务提供者启动类
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.hanxw.project.order.mapper")
@ComponentScan(basePackages = {
        "com.hanxw.project"   // 扫描整个项目根包
})
public class OrderProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderProviderApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Order Provider Started (Port:8082) ");
        System.out.println("=====================================");
    }
}
