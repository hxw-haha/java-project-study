package com.hanxw.project.common.redis.limiter;

import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.exception.BizException;
import com.hanxw.project.common.redis.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 限流拦截器
 */
@Component
public class RateLimiterInterceptor implements HandlerInterceptor {
    
    @Autowired
    private RedisRateLimiter limiter;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        RateLimit rl = ((HandlerMethod) handler).getMethodAnnotation(RateLimit.class);
        if (rl == null) {
            return true;
        }

        boolean ok = limiter.tryAcquire(rl.key(), rl.max(), rl.period(), rl.unit());
        if (!ok) {
            throw new BizException(ErrorCode.PARAM_ERROR.setMessage("请求太频繁"));
        }
        return true;
    }
}
