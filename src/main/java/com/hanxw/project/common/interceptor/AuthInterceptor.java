package com.hanxw.project.common.interceptor;

import com.hanxw.project.common.LoginRequired;
import com.hanxw.project.common.context.RequestContext;
import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.exception.BizException;
import com.hanxw.project.common.redis.login.JwtUtil;
import com.hanxw.project.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private CacheService cacheService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 非 Controller 请求直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;

        LoginRequired methodAnno =
                method.getMethodAnnotation(LoginRequired.class);

        LoginRequired classAnno =
                method.getBeanType().getAnnotation(LoginRequired.class);

        if (methodAnno == null && classAnno == null) {
            return true; // 不需要登录
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new BizException(ErrorCode.PARAM_ERROR.setMessage("未登录"));
        }
        Long userId = JwtUtil.parseUserId(token);

        String cachedToken =
                cacheService.get("login:token:" + userId, String.class);

        if (!token.equals(cachedToken)) {
            throw new BizException(ErrorCode.PARAM_ERROR.setMessage("登录失效"));
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
