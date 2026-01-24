package com.hanxw.project.common.enums;

/**
 * 错误码枚举
 */
public enum ErrorCode {

    SUCCESS(0, "成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "系统异常"),

    // RPC 相关错误码
    RPC_ERROR(2000, "远程服务调用失败"),
    RPC_TIMEOUT(2001, "远程服务调用超时"),
    SERVICE_UNAVAILABLE(2002, "服务暂不可用"),
    SERVICE_DEGRADED(2003, "服务已降级"),

    // 用户模块
    USER_NOT_FOUND(3001, "用户不存在"),

    // 订单模块
    ORDER_NOT_FOUND(4001, "订单不存在");

    private final int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode setMessage(String message) {
        this.message = message;
        return this;
    }
}
