package com.hanxw.project.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API 日志切面
 * 适配微服务架构：支持不同模块的 controller 包路径
 */
@Aspect
@Component
public class ApiLogAspect {
    private static final Logger log = LoggerFactory.getLogger(ApiLogAspect.class);

    /**
     * 匹配所有模块的 controller 包
     * - project-web: com.hanxw.project.web.controller
     * - provider-user: com.hanxw.project.user.controller (如果有)
     * - provider-order: com.hanxw.project.order.controller (如果有)
     */
    @Around("execution(* com.hanxw.project..controller..*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long cost = System.currentTimeMillis() - start;

        log.info("API {} cost {} ms", pjp.getSignature().toShortString(), cost);
        return result;
    }
}
