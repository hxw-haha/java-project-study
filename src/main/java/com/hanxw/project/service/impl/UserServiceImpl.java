package com.hanxw.project.service.impl;

import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.exception.BizException;
import com.hanxw.project.entity.UserEntity;
import com.hanxw.project.mapper.UserMapper;
import com.hanxw.project.service.CacheService;
import com.hanxw.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    public static final String USER_CACHE_KEY = "user:detail:";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public UserEntity getUserById(Long id) {
        if (id <= 0) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        return getUserDetail(id);
    }

    @Transactional
    @Override
    public void updateUser(UserEntity user) {
//        userMapper.updateById(user);
        String key = USER_CACHE_KEY + user.getId();
        cacheService.delete(key);
    }

    public UserEntity getUserDetail(Long userId) {
        String key = USER_CACHE_KEY + userId;
        UserEntity cached = cacheService.get(key, UserEntity.class);
        if (cached != null) {
            return cached;
        }
        // 缓存未命中
        UserEntity entity = userMapper.selectById(userId);
        if (entity == null) {
            // 缓存空对象避免穿透
            cacheService.set(key, new UserEntity(), 60, TimeUnit.SECONDS);

            throw new BizException(ErrorCode.NOT_FOUND);
        }
        cacheService.set(key, entity, 1, TimeUnit.HOURS);
        return entity;
    }

}
