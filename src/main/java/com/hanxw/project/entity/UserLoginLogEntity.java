package com.hanxw.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("user_login_log")
public class UserLoginLogEntity extends BaseEntity {

    private Long userId;
    private String loginIp;
    private String userAgent;
    private Date loginTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
