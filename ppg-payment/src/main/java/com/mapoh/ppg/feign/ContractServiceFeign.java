package com.mapoh.ppg.feign;

import com.mapoh.ppg.feign.hystrix.ContractFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/19 21:38
 */

@FeignClient(value = "eureka-client-ppg-contract",
fallback = ContractFeignHystrix.class)
public interface ContractServiceFeign {

    @GetMapping("/amount")
    public BigDecimal getAmount(@RequestParam Long contractId);

    @PostMapping("/validContract")
    public Boolean validContract(@RequestParam Long contractId);
}
