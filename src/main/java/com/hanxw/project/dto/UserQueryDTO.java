package com.hanxw.project.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserQueryDTO {

    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long id;
    @Min(value = 1, message = "页码须大于0")
    private int page;
    @Min(value = 1, message = "每页数量须大于0")
    private int pageSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
