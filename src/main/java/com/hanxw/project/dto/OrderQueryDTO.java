package com.hanxw.project.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderQueryDTO {
    @NotNull(message = "订单ID不能为空")
    @Min(value = 1, message = "订单ID必须大于0")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
