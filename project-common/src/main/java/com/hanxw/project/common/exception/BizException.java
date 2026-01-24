package com.hanxw.project.common.exception;

import com.hanxw.project.common.enums.ErrorCode;

/**
 * 业务异常
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;
    private final String msg;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
