package com.hanxw.project.order.service;

import com.hanxw.project.api.UserRpcService;
import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户服务降级 Mock
 * 
 * 当 UserRpcService 不可用时，使用此降级实现
 */
public class UserRpcServiceMock implements UserRpcService {

    private static final Logger log = LoggerFactory.getLogger(UserRpcServiceMock.class);

    @Override
    public Result<UserDTO> getUserById(Long id) {
        log.warn("[降级] getUserById, id={}", id);
        return Result.fail(ErrorCode.SERVICE_DEGRADED);
    }

    @Override
    public Result<Boolean> updateUser(UserDTO userDTO) {
        log.warn("[降级] updateUser");
        return Result.fail(ErrorCode.SERVICE_DEGRADED);
    }

    @Override
    public Result<Boolean> existsById(Long id) {
        log.warn("[降级] existsById, id={}", id);
        // 降级时假设用户存在，不阻断业务
        return Result.success(true);
    }
}
