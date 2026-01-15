package com.hanxw.project.service.impl;

import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.exception.BizException;
import com.hanxw.project.entity.UserEntity;
import com.hanxw.project.mapper.UserMapper;
import com.hanxw.project.common.redis.lock.RedisDistributedLock;
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

    @Autowired
    private RedisDistributedLock redisDistributedLock;

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
        if (cached != null) return cached;

        //缓存重建（防击穿）
        String lockKey = "lock:user:" + userId;
        boolean locked = redisDistributedLock.tryLock(lockKey, 0, 5, TimeUnit.SECONDS);
        if (locked) {
            try {
                // double check
                cached = cacheService.get(key, UserEntity.class);
                if (cached != null) return cached;
                UserEntity entity = userMapper.selectById(userId);
                if (entity == null) return null;
                cacheService.set(key, entity, 1, TimeUnit.HOURS);
                return entity;
            } finally {
                redisDistributedLock.unlock(lockKey);
            }
        } else {
            // 等待锁释放后从缓存读取
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cacheService.get(key, UserEntity.class);
        }
    }


}
