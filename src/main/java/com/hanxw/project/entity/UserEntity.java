package com.hanxw.project.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserEntity {

    private Long id;
    private String username;
    private String password;
    private LocalDateTime createTime;
    // 一对多：用户的所有订单（用于复杂映射演示）
    private List<OrderEntity> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }
}
