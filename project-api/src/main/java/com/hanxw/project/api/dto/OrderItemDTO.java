package com.hanxw.project.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单项 DTO
 */
@Data
public class OrderItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
