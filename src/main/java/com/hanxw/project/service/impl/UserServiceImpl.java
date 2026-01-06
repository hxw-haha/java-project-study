package com.hanxw.project.service.impl;

import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.exception.BizException;
import com.hanxw.project.entity.UserEntity;
import com.hanxw.project.mapper.UserMapper;
import com.hanxw.project.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserEntity getUserById(Long id) {
        if (id <= 0) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        UserEntity entity = userMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        return entity;
    }
}
