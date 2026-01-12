package com.hanxw.project.controller;

import com.hanxw.project.common.result.Result;
import com.hanxw.project.dto.LoginDTO;
import com.hanxw.project.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserLoginLogService service;

    /**
     * Content-Type: application/json
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO dto, HttpServletRequest httpRequest) {
        service.recordLoginLog(dto.getUserId(), httpRequest);
        return Result.success("userId:" + dto.getUserId());
    }

}
