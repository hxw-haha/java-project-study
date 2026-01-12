package com.hanxw.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanxw.project.entity.UserLoginLogEntity;
import com.hanxw.project.mapper.UserLoginLogMapper;
import com.hanxw.project.service.UserLoginLogService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLogEntity> implements UserLoginLogService {

    @Override
    public void recordLoginLog(Long userId, HttpServletRequest request) {
        UserLoginLogEntity log = new UserLoginLogEntity();
        log.setUserId(userId);
        log.setLoginIp(getClientIpAddress(request)); // 推荐用更健壮的方式获取 IP
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setLoginTime(new Date());
        save(log);
    }

    // 更可靠的获取客户端 IP 的方法（考虑代理）
    private String getClientIpAddress(HttpServletRequest request) {
        String xip = request.getHeader("X-Real-IP");
        String xfor = request.getHeader("X-Forwarded-For");
        if (xfor != null && !xfor.isEmpty() && !"unknown".equalsIgnoreCase(xfor)) {
            int index = xfor.indexOf(",");
            if (index != -1) {
                return xfor.substring(0, index);
            } else {
                return xfor;
            }
        }
        if (xip != null && !xip.isEmpty() && !"unknown".equalsIgnoreCase(xip)) {
            return xip;
        }
        return request.getRemoteAddr();
    }
}
