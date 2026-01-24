package com.hanxw.project.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户 DTO（RPC 传输对象）
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private LocalDateTime createTime;
}
