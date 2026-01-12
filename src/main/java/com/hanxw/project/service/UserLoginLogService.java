package com.hanxw.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanxw.project.entity.UserLoginLogEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserLoginLogService extends IService<UserLoginLogEntity> {
    void recordLoginLog(Long userId, HttpServletRequest request);
}
