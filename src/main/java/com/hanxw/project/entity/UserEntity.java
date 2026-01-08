package com.hanxw.project.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserEntity {

    private Long id;
    private String username;
    private String password;
    private LocalDateTime createTime;
    // 一对多：用户的所有订单（用于复杂映射演示）
    private List<OrderEntity> orders;
}
