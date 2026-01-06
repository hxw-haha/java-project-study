package com.hanxw.project.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class ApiLogAspect {
    private static final Logger log =
            LoggerFactory.getLogger(ApiLogAspect.class);

    @Around("execution(* com.hanxw.project.controller..*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long cost = System.currentTimeMillis() - start;

        log.info("API {} cost {} ms",
                pjp.getSignature().toShortString(), cost);

        return result;
    }
}
