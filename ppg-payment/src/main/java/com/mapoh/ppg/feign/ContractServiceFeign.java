package com.mapoh.ppg.feign;

import com.mapoh.ppg.feign.hystrix.ContractFeignHystrix;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/19 21:38
 */

@FeignClient(value = "eureka-client-contract-distribution",
fallback = ContractFeignHystrix.class)
public interface ContractServiceFeign {

    @GetMapping("ppg-contract/distribution/amount/{contractId}")
    public CommonResponse<BigDecimal> getAmount(@PathVariable("contractId") Long contractId);

    @PostMapping("ppg-contract/distribution/validContract/{contractId}")
    public CommonResponse<Boolean> validContract(@PathVariable("contractId") Long contractId);

    @GetMapping("ppg-contract/distribution/unitamount/{contractId}")
    public CommonResponse<BigDecimal> getUnitAmount(@PathVariable("contractId") Long contractId);
}
