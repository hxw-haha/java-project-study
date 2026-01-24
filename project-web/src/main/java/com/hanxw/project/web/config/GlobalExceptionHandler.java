package com.hanxw.project.web.config;

import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.exception.BizException;
import com.hanxw.project.common.result.Result;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理（复用原有逻辑）
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException e) {
        log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMsg());
        return Result.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(RpcException.class)
    public Result<?> handleRpcException(RpcException e) {
        log.error("RPC调用异常: {}", e.getMessage(), e);
        if (e.isTimeout()) {
            return Result.fail(ErrorCode.RPC_TIMEOUT);
        }
        return Result.fail(ErrorCode.RPC_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.fail(ErrorCode.SYSTEM_ERROR);
    }
}
