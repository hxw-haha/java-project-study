package com.hanxw.project.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体（复用原有结构）
 */
@Data
@TableName("t_order")
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String orderNo;
    private BigDecimal amount;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<OrderItemEntity> items;
}
