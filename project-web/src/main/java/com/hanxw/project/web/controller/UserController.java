package com.hanxw.project.web.controller;

import com.hanxw.project.api.UserRpcService;
import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.constants.DubboConstant;
import com.hanxw.project.common.result.Result;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口（基于原有 UserController 改造）
 * 
 * 通过 Dubbo RPC 调用 provider-user
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 引用用户 RPC 服务
     */
    @DubboReference(
        version = DubboConstant.VERSION,
        group = DubboConstant.GROUP_USER,
        check = false,
        timeout = DubboConstant.TIMEOUT_DEFAULT,
        retries = DubboConstant.RETRIES_DEFAULT,
        mock = "com.hanxw.project.web.mock.UserRpcServiceMock"
    )
    private UserRpcService userRpcService;

    /**
     * 根据ID查询用户（原有接口）
     */
    @GetMapping("/{id}")
    public Result<UserDTO> getUser(@PathVariable Long id) {
        return userRpcService.getUserById(id);
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result<Boolean> updateUser(@RequestBody UserDTO userDTO) {
        return userRpcService.updateUser(userDTO);
    }

    /**
     * 检查用户是否存在
     */
    @GetMapping("/exists/{id}")
    public Result<Boolean> existsById(@PathVariable Long id) {
        return userRpcService.existsById(id);
    }
}
