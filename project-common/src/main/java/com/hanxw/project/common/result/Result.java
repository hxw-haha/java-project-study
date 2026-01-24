package com.hanxw.project.common.result;

import com.hanxw.project.common.enums.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果（支持RPC序列化）
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 0;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = msg;
        return r;
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return fail(errorCode.getCode(), errorCode.getMessage());
    }

    public boolean isSuccess() {
        return this.code == 0;
    }
}
