package com.hanxw.project.common.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具类
 */
@Component
public class RedisDistributedLock {

    private final RedissonClient client;

    public RedisDistributedLock(RedissonClient client) {
        this.client = client;
    }

    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = client.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void unlock(String key) {
        RLock lock = client.getLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}


