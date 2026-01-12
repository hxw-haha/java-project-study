package com.hanxw.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanxw.project.common.result.Result;
import com.hanxw.project.dto.LoginLogQueryDTO;
import com.hanxw.project.entity.UserLoginLogEntity;
import com.hanxw.project.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/loginLog")
public class LoginLogController {
    @Autowired
    private UserLoginLogService userLoginLogService;

    @GetMapping("/detail")
    public Result<List<UserLoginLogEntity>> detail(@Valid LoginLogQueryDTO dto) {
        Page<UserLoginLogEntity> page = userLoginLogService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<UserLoginLogEntity>()
                        .eq(dto.getUserId() != null,
                                UserLoginLogEntity::getUserId,
                                dto.getUserId())
                        .orderByDesc(UserLoginLogEntity::getLoginTime)
        );
        return Result.success(page.getRecords());
    }
}
