package com.hanxw.project.common.exception;

import com.hanxw.project.common.enums.ErrorCode;

public class BizException extends RuntimeException {

    private final ErrorCode errorCode;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getCode() {
        return errorCode.getCode();
    }
}
