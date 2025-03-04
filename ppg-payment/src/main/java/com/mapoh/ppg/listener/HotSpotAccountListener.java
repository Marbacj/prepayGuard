package com.mapoh.ppg.listener;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author mabohv
 * @date 2025/2/28 11:34
 * detect the hot merchant account
 * 如果每分钟交易次数超过五次则可以认为是热点用户
 */

@Service
public class HotSpotAccountListener {

    private RedisTemplate<String, Long> redisTemplate;

    private static final String HOTSPOT_KEY_PREFIX = "hotspot:merchant";
    private static final int FREQUENCY_THRSHHOLD = 10;
    private static final int WINDOW_SECONDS = 60;

    public boolean isHotSpot(Long merchant){
        String key = HOTSPOT_KEY_PREFIX + merchant;
        Long count = redisTemplate.opsForValue().increment(key,1);
        redisTemplate.expire(key, WINDOW_SECONDS, TimeUnit.SECONDS);

        return count != null && count >= FREQUENCY_THRSHHOLD;
    }
}
