package com.hanxw.project.common.interceptor;

import com.hanxw.project.common.context.RequestContext;
import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.exception.BizException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String userId = request.getHeader("X-User-Id");
        if (userId == null) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        RequestContext.setUserId(Long.valueOf(userId));
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
