package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.feign.PaymentServiceFeign;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.stereotype.Component;

/**
 * @author mabohv
 * @date 2025/4/9 18:27
 */

@Component
public class PaymentServiceHystrix implements PaymentServiceFeign {
    @Override
    public CommonResponse<Double> getMerchantTodayIncome(Long merchantId) {
        return CommonResponse.errorResponse(
                -1,
                "invoke failed",
                0.0
        );
    }
}
