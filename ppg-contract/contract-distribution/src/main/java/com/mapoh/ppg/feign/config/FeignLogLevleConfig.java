package com.mapoh.ppg.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mabohv
 * @date 2025/1/12 22:12
 */

@Configuration
public class FeignLogLevleConfig {

    //采用full打印日志
    @Bean
    public Logger.Level configLog(){
        return Logger.Level.FULL;
    }
}
