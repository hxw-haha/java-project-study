package com.hanxw.project.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LoginLogQueryDTO {
    @NotNull(message = "userId不能为空")
    @Min(value = 1, message = "userId必须大于0")
    private Long userId;
    private int page = 1;
    private int size = 20;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

