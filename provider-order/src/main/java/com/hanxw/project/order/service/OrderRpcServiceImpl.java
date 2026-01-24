package com.hanxw.project.order.service;

import com.hanxw.project.api.OrderRpcService;
import com.hanxw.project.api.UserRpcService;
import com.hanxw.project.api.dto.OrderDTO;
import com.hanxw.project.api.dto.OrderItemDTO;
import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.constants.DubboConstant;
import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.result.Result;
import com.hanxw.project.order.entity.OrderEntity;
import com.hanxw.project.order.entity.OrderItemEntity;
import com.hanxw.project.order.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务 RPC 实现
 * 
 * 基于原有 OrderServiceImpl 改造
 * 演示：服务间 RPC 调用（调用 UserRpcService）
 */
@DubboService(
    version = DubboConstant.VERSION,
    group = DubboConstant.GROUP_ORDER,
    timeout = DubboConstant.TIMEOUT_DEFAULT,
    retries = DubboConstant.RETRIES_NONE  // 写操作不重试
)
public class OrderRpcServiceImpl implements OrderRpcService {

    private static final Logger log = LoggerFactory.getLogger(OrderRpcServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 引用用户服务（跨服务 RPC 调用）
     */
    @DubboReference(
        version = DubboConstant.VERSION,
        group = DubboConstant.GROUP_USER,
        check = false,
        timeout = 2000,
        retries = 2,
        mock = "com.hanxw.project.order.service.UserRpcServiceMock"
    )
    private UserRpcService userRpcService;

    @Override
    public Result<UserDTO> selectUserWithOrders(Long userId) {
        log.info("[OrderRpcService] selectUserWithOrders, userId={}", userId);

        // 1. 调用用户服务获取用户信息
        Result<UserDTO> userResult = userRpcService.getUserById(userId);
        if (!userResult.isSuccess()) {
            return userResult;
        }

        // 2. 查询用户订单
        List<OrderEntity> orders = orderMapper.selectByUserId(userId);
        
        // 这里简化处理，原有方法返回的是包含订单的用户对象
        // 实际可以扩展 UserDTO 增加 orders 字段
        
        return userResult;
    }

    @Override
    public Result<OrderDTO> getOrderById(Long orderId) {
        log.info("[OrderRpcService] getOrderById, orderId={}", orderId);

        if (orderId == null || orderId <= 0) {
            return Result.fail(ErrorCode.PARAM_ERROR);
        }

        OrderEntity entity = orderMapper.selectById(orderId);
        if (entity == null) {
            return Result.fail(ErrorCode.ORDER_NOT_FOUND);
        }

        return Result.success(convertToDTO(entity));
    }

    @Override
    public Result<List<OrderDTO>> listOrdersByUserId(Long userId) {
        log.info("[OrderRpcService] listOrdersByUserId, userId={}", userId);

        // 先校验用户是否存在（跨服务调用）
        Result<Boolean> existsResult = userRpcService.existsById(userId);
        if (!existsResult.isSuccess() || !Boolean.TRUE.equals(existsResult.getData())) {
            log.warn("用户不存在, userId={}", userId);
            return Result.fail(ErrorCode.USER_NOT_FOUND);
        }

        List<OrderEntity> orders = orderMapper.selectByUserId(userId);
        List<OrderDTO> dtos = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(dtos);
    }

    private OrderDTO convertToDTO(OrderEntity entity) {
        if (entity == null) return null;
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(entity, dto);

        // 转换订单项
        if (entity.getItems() != null) {
            List<OrderItemDTO> items = entity.getItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList());
            dto.setItems(items);
        }

        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItemEntity entity) {
        if (entity == null) return null;
        OrderItemDTO dto = new OrderItemDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
