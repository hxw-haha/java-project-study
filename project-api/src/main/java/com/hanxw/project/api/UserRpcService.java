package com.hanxw.project.api;

import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.result.Result;

/**
 * 用户服务 RPC 接口
 * 
 * 基于现有 UserService 进行 RPC 改造
 */
public interface UserRpcService {

    /**
     * 根据ID查询用户
     */
    Result<UserDTO> getUserById(Long id);

    /**
     * 更新用户
     */
    Result<Boolean> updateUser(UserDTO userDTO);

    /**
     * 检查用户是否存在
     */
    Result<Boolean> existsById(Long id);
}
