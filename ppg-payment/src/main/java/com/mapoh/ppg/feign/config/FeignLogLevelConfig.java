package com.mapoh.ppg.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mabohv
 * @date 2025/2/8 14:03
 */

@Configuration
public class FeignLogLevelConfig {

    //采用full打印日志
    @Bean
    public Logger.Level configLog(){
        return Logger.Level.FULL;
    }
}