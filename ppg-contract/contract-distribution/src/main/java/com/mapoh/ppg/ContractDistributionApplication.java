package com.mapoh.ppg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author mabohv
 * @date 2025/1/8 19:17
 */

@SpringBootApplication
@EnableFeignClients
public class ContractDistributionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractDistributionApplication.class);
    }
}
