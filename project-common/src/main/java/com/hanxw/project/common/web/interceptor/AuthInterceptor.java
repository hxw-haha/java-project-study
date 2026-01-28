package com.hanxw.project.common.web.interceptor;

import com.hanxw.project.common.web.annotation.LoginRequired;
import com.hanxw.project.common.web.context.RequestContext;
import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.exception.BizException;
import com.hanxw.project.common.security.jwt.JwtUtil;
import com.hanxw.project.common.redis.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录认证拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private CacheService cacheService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        LoginRequired methodAnno = method.getMethodAnnotation(LoginRequired.class);
        LoginRequired classAnno = method.getBeanType().getAnnotation(LoginRequired.class);

        if (methodAnno == null && classAnno == null) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new BizException(ErrorCode.UNAUTHORIZED);
        }

        Long userId = JwtUtil.parseUserId(token);
        String cachedToken = cacheService.get("login:token:" + userId, String.class);

        if (!token.equals(cachedToken)) {
            throw new BizException(ErrorCode.UNAUTHORIZED.setMessage("登录失效"));
        }

        RequestContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        RequestContext.clear();
    }
}

