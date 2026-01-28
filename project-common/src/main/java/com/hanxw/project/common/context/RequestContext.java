package com.hanxw.project.common.context;

/**
 * 请求上下文（ThreadLocal）
 * 用于在请求线程中传递用户ID等信息
 */
public class RequestContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void clear() {
        USER_ID.remove();
    }
}
