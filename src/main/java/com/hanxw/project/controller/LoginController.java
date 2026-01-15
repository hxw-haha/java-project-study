package com.hanxw.project.controller;

import com.hanxw.project.common.result.Result;
import com.hanxw.project.dto.LoginDTO;
import com.hanxw.project.common.redis.RateLimit;
import com.hanxw.project.common.redis.login.JwtUtil;
import com.hanxw.project.service.CacheService;
import com.hanxw.project.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserLoginLogService service;
    @Autowired
    private CacheService cacheService;

    /**
     * Content-Type: application/json
     */
    @PostMapping("/login")
    @RateLimit(key = "login", max = 5, period = 1, unit = TimeUnit.MINUTES)
    public Result<String> login(@RequestBody LoginDTO dto, HttpServletRequest httpRequest) {
        service.recordLoginLog(dto.getUserId(), httpRequest);
        String generate = JwtUtil.generate(dto.getUserId());
        cacheService.set("login:token:" + dto.getUserId(), generate, 1, TimeUnit.HOURS);
        return Result.success(generate);
    }

}
