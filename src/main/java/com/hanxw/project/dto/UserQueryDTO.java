package com.hanxw.project.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserQueryDTO {

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
