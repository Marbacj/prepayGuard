package com.mapoh.ppg.feign;

import com.mapoh.ppg.feign.hystrix.UserServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/19 21:39
 */

@FeignClient(value = "eureka-client-ppg-user",
fallback = UserServiceHystrix.class)
public interface UserServiceFeign {

    @GetMapping("/balance")
    BigDecimal getBalance(Long userId);

    @PostMapping("/settlement")
    Boolean settlement(Long userId, BigDecimal amount);
}
