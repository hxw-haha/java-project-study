package com.hanxw.project.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 链路追踪拦截器（预留接口）
 * TODO: 实现分布式链路追踪功能（如集成 SkyWalking、Zipkin 等）
 */
public class TraceInterceptor implements HandlerInterceptor {
    // TODO: 实现链路追踪功能
}
