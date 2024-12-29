package com.mapoh.ppg.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author mabohv
 * @date 2024/12/26 15:11
 */

@Component
public class RedisUtil{

    private static final int DEFAULT_EXPIRE_TIME = 60 * 1000;

    public static StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static void set(String key, String value, long expireTime) {
        redisTemplate.opsForValue().set(key, value,expireTime, TimeUnit.MILLISECONDS);
    }

    public static String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static void del(String key) {
        redisTemplate.delete(key);
    }
}
