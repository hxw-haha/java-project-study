package com.hanxw.project.web.service;

import com.hanxw.project.api.OrderRpcService;
import com.hanxw.project.api.UserRpcService;
import com.hanxw.project.api.dto.OrderDTO;
import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.constants.DubboConstant;
import com.hanxw.project.common.result.Result;
import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.ai.mcp.annotation.McpTool;
//import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
public class DubboMcpTools {
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

    /*@McpTool(
            name = "get_user_info",
            description = "根据用户ID查询用户信息（Dubbo 服务）"
    )
    public Result<UserDTO> getUserInfo(
            @McpToolParam(description = "用户ID，必填", required = true) Long userId) {
        return userRpcService.getUserById(userId);
    }

    @McpTool(
            name = "create_order",
            description = "为指定用户创建订单，支持商品列表（Dubbo 服务）"
    )
    public OrderDTO createOrder(
            @McpToolParam(description = "用户ID，必填", required = true) Long userId,
            @McpToolParam(description = "商品ID列表，逗号分隔", required = true) String productIds,
            @McpToolParam(description = "订单总金额（元）", required = true) Double totalAmount) {
        //TODO
        return new OrderDTO();
    }

    @McpTool(
            name = "get_order_info",
            description = "根据订单ID查询订单详情（Dubbo 服务）"
    )
    public Result<OrderDTO> getOrderInfo(
            @McpToolParam(description = "订单ID，必填", required = true) Long orderId) {
        return orderRpcService.getOrderById(orderId);
    }*/
}
