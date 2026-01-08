package com.hanxw.project.controller;

import com.hanxw.project.common.result.Result;
import com.hanxw.project.dto.UserQueryDTO;
import com.hanxw.project.entity.UserEntity;
import com.hanxw.project.service.OrderService;
import com.hanxw.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public Result<UserEntity> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @GetMapping("/detail")
    public Result<UserEntity> detail(@Valid UserQueryDTO dto) {
        return Result.success(userService.getUserById(dto.getId()));
    }

    // 在 UserController 中添加
    @GetMapping("/{id}/orders")
    public Result<UserEntity> getUserOrders(@PathVariable Long id) {
        UserEntity user = orderService.getUserWithOrders(id);
        return Result.success(user);
    }
}
