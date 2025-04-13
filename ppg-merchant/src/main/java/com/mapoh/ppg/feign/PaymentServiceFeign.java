package com.mapoh.ppg.feign;

import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/4/5 21:18
 */

@FeignClient(value = "eureka-client-ppg-transaction")
public interface PaymentServiceFeign {

    @GetMapping("ppg-payment/getMerchantTodayIncome")
    public CommonResponse<Double> getMerchantTodayIncome(Long merchantId);
}
