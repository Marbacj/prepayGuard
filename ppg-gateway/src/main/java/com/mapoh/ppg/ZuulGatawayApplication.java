package com.mapoh.ppg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author mabohv
 * @date 2024/12/24 17:19
 */

@EnableZuulProxy
@SpringBootApplication
public class ZuulGatawayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatawayApplication.class, args);
    }
}
