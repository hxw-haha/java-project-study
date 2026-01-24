package com.hanxw.project.web.controller;

import com.hanxw.project.api.OrderRpcService;
import com.hanxw.project.api.dto.OrderDTO;
import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.constants.DubboConstant;
import com.hanxw.project.common.result.Result;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单接口（基于原有 OrderController 改造）
 * 
 * 通过 Dubbo RPC 调用 provider-order
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 引用订单 RPC 服务
     */
    @DubboReference(
        version = DubboConstant.VERSION,
        group = DubboConstant.GROUP_ORDER,
        check = false,
        timeout = DubboConstant.TIMEOUT_DEFAULT,
        retries = DubboConstant.RETRIES_NONE,
        mock = "com.hanxw.project.web.mock.OrderRpcServiceMock"
    )
    private OrderRpcService orderRpcService;

    /**
     * 查询用户及其订单（原有接口）
     */
    @GetMapping("/user/{userId}/with-orders")
    public Result<UserDTO> getUserWithOrders(@PathVariable Long userId) {
        return orderRpcService.selectUserWithOrders(userId);
    }

    /**
     * 根据订单ID查询
     */
    @GetMapping("/{id}")
    public Result<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderRpcService.getOrderById(id);
    }

    /**
     * 查询用户的订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<OrderDTO>> listOrdersByUserId(@PathVariable Long userId) {
        return orderRpcService.listOrdersByUserId(userId);
    }
}
