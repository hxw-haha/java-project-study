package com.hanxw.project.common.constants;

/**
 * Dubbo 常量
 */
public class DubboConstant {

    private DubboConstant() {}

    // 服务分组
    public static final String GROUP_USER = "user-group";
    public static final String GROUP_ORDER = "order-group";

    // 服务版本
    public static final String VERSION = "1.0.0";

    // 超时配置 (ms)
    public static final int TIMEOUT_DEFAULT = 3000;
    public static final int TIMEOUT_LONG = 10000;

    // 重试配置
    public static final int RETRIES_DEFAULT = 2;
    public static final int RETRIES_NONE = 0;  // 写操作不重试
}
