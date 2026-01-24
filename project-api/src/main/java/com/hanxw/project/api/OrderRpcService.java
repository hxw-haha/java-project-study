package com.hanxw.project.api;

import com.hanxw.project.api.dto.OrderDTO;
import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.result.Result;

import java.util.List;

/**
 * 订单服务 RPC 接口
 * 
 * 基于现有 OrderService 进行 RPC 改造
 */
public interface OrderRpcService {

    /**
     * 查询用户及其订单（原有功能）
     */
    Result<UserDTO> selectUserWithOrders(Long userId);

    /**
     * 根据订单ID查询订单
     */
    Result<OrderDTO> getOrderById(Long orderId);

    /**
     * 查询用户的订单列表
     */
    Result<List<OrderDTO>> listOrdersByUserId(Long userId);
}
