package com.mapoh.ppg.feign;

import com.mapoh.ppg.dto.payment.SettlementRequest;
import com.mapoh.ppg.feign.hystrix.UserServiceHystrix;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/19 21:39
 */

@FeignClient(value = "eureka-client-ppg-user",
fallback = UserServiceHystrix.class)
public interface UserServiceFeign {

    @GetMapping("ppg-user/balance/{userId}")
    CommonResponse<BigDecimal> getBalance(@PathVariable("userId") Long userId);

    @PostMapping("ppg-user/settlement")
    CommonResponse<Boolean> settlement(@RequestBody SettlementRequest settlementRequest) ;
}
