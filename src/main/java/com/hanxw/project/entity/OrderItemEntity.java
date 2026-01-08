package com.hanxw.project.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemEntity {
    private Long id;
    private Long orderId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
