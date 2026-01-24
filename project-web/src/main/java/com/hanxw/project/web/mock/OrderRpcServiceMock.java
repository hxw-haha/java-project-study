package com.hanxw.project.web.mock;

import com.hanxw.project.api.OrderRpcService;
import com.hanxw.project.api.dto.OrderDTO;
import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 订单服务降级 Mock（Web层）
 */
public class OrderRpcServiceMock implements OrderRpcService {

    private static final Logger log = LoggerFactory.getLogger(OrderRpcServiceMock.class);

    @Override
    public Result<UserDTO> selectUserWithOrders(Long userId) {
        log.warn("[Web降级] selectUserWithOrders, userId={}", userId);
        return Result.fail(ErrorCode.SERVICE_DEGRADED);
    }

    @Override
    public Result<OrderDTO> getOrderById(Long orderId) {
        log.warn("[Web降级] getOrderById, orderId={}", orderId);
        return Result.fail(ErrorCode.SERVICE_DEGRADED);
    }

    @Override
    public Result<List<OrderDTO>> listOrdersByUserId(Long userId) {
        log.warn("[Web降级] listOrdersByUserId, userId={}", userId);
        return Result.fail(ErrorCode.SERVICE_DEGRADED);
    }
}
