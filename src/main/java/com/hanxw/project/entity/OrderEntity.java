package com.hanxw.project.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderEntity {
    private Long id;
    private Long userId;
    private String orderNo;
    private BigDecimal amount;
    private LocalDateTime createTime;

    // 一对多：订单的所有订单项
    private List<OrderItemEntity> items;
}
