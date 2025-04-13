package com.mapoh.ppg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author mabohv
 * @date 2025/3/11 08:47
 * 过期事件监听
 */

@Configuration
public class RedisExpirationConfig {
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        //Redis消息监听器
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        //设置Redis链接工厂
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}