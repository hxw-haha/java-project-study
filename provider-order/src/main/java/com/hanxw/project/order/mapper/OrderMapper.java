package com.hanxw.project.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanxw.project.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单 Mapper（复用原有XML映射）
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {

    /**
     * 查询用户及其订单（原有复杂查询）
     */
    OrderEntity selectUserWithOrders(@Param("userId") Long userId);

    /**
     * 根据用户ID查询订单列表
     */
    List<OrderEntity> selectByUserId(@Param("userId") Long userId);
}
