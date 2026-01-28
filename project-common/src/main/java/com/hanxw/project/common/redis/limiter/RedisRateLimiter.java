package com.hanxw.project.common.redis.limiter;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Redis 限流器
 */
@Component
public class RedisRateLimiter {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisRateLimiter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryAcquire(String key, long maxTokens, long refillTime, TimeUnit unit) {
        long now = System.currentTimeMillis();
        long expire = unit.toMillis(refillTime);

        String tokenKey = key + ":tokens";
        String timestampKey = key + ":timestamp";

        long lastTime = Optional.ofNullable(redisTemplate.opsForValue().get(timestampKey))
                .map(v -> Long.parseLong(v.toString()))
                .orElse(now);

        long tokens = Optional.ofNullable(redisTemplate.opsForValue().get(tokenKey))
                .map(v -> Long.parseLong(v.toString()))
                .orElse(maxTokens);

        long delta = Math.max(0, now - lastTime);
        long refill = delta * maxTokens / expire;
        tokens = Math.min(tokens + refill, maxTokens);

        if (tokens > 0) {
            redisTemplate.opsForValue().set(tokenKey, String.valueOf(tokens - 1));
            redisTemplate.opsForValue().set(timestampKey, String.valueOf(now));
            return true;
        }
        redisTemplate.opsForValue().set(timestampKey, String.valueOf(now));
        return false;
    }
}
