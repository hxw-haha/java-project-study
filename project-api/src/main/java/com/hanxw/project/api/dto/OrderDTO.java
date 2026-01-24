package com.hanxw.project.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单 DTO（RPC 传输对象）
 */
@Data
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String orderNo;
    private BigDecimal amount;
    private LocalDateTime createTime;

    // 订单项
    private List<OrderItemDTO> items;
}
