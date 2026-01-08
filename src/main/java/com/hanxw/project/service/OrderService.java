package com.hanxw.project.service;

import com.hanxw.project.entity.UserEntity;

public interface OrderService {
    UserEntity selectUserWithOrders(Long userId);
}
