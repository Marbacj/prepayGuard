package com.mapoh.ppg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author mabohv
 * @date 2025/1/4 09:56
 */

@EnableEurekaClient
@EnableJpaAuditing
@SpringBootApplication
public class ContractTemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractTemplateApplication.class, args);
    }
}
