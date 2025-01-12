package com.mapoh.ppg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author mabohv
 * @date 2025/1/8 19:17
 */

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableEurekaClient
@EnableJpaAuditing
public class ContractDistributionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractDistributionApplication.class);
    }
}
