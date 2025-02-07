package com.mapoh.ppg.utils;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author mabohv
 * @date 2025/2/5 21:26
 */

@Component
public class RedisDelayedQueue {

    @Resource
    RedissonClient redissonClient;

    public static Logger logger = LoggerFactory.getLogger(RedisDelayedQueue.class);

    /**
     *
     *
     * @param t     DTO传输对象
     * @param delay
     * @param timeUnit
     * @param queueName
     * @param <T>
     */
    public <T> void addQueue(T t, long delay, TimeUnit timeUnit, String queueName) {
        logger.info("添加队列:{}, delay:{}, timeUnit:{}", queueName, delay, timeUnit);
        RBlockingQueue<T> rBlockingQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(rBlockingQueue);
        delayedQueue.offer(t, delay, timeUnit);
    }
}
