package com.ac.su.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String LOCK_PREFIX = "lock:";
    // 락 생성
    public boolean acquireLock(String key, long timeout) {
        String lockKey = LOCK_PREFIX + key;
         Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", timeout, TimeUnit.SECONDS);
         return success != null && success;
    }
    // 락 해제
    public void releaseLock(String key) {
        String lockKey = LOCK_PREFIX + key;
        redisTemplate.delete(lockKey);
    }
}
