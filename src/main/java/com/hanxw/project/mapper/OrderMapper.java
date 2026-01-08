package com.hanxw.project.mapper;


import com.hanxw.project.entity.OrderEntity;
import com.hanxw.project.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderMapper {

    // 简单插入（测试用）
    int insert(OrderEntity order);

    // 复杂查询：根据用户ID查询用户及其所有订单（关联查询）
    UserEntity selectUserWithOrders(Long userId);

    // 复杂查询：动态条件查询订单列表（支持分页）
    List<OrderEntity> selectOrdersDynamic(@Param("userId") Long userId,
                                          @Param("minAmount") BigDecimal minAmount,
                                          @Param("productNames") List<String> productNames,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit);

    // 一对多嵌套结果：查询订单及其订单项
    List<OrderEntity> selectOrdersWithItems(@Param("orderIds") List<Long> orderIds);
}