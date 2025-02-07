package com.mapoh.ppg.config;

/**
 * @author mabohv
 * @date 2025/2/5 21:25
 */

public interface RedisDelayedQueueListener<T> {

    void invoke(T t);
}
