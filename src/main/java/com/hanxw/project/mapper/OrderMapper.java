package com.hanxw.project.mapper;

import com.hanxw.project.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    // 复杂查询：根据用户ID查询用户及其所有订单（关联查询）
    UserEntity selectUserWithOrders(Long userId);
}