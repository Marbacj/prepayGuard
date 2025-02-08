package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.dto.payment.SettlementRequest;
import com.mapoh.ppg.feign.UserServiceFeign;
import com.mapoh.ppg.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/19 22:49
 */

@Component
public class UserServiceHystrix implements UserServiceFeign {

    public static Logger logger = LoggerFactory.getLogger(UserServiceHystrix.class);


    @Override
    public CommonResponse<BigDecimal> getBalance(Long userId) {

        logger.error("[eureka-client-ppg-user] getBalance error;{}", userId);
        return null;
    }

    @Override
    public CommonResponse<Boolean> settlement(SettlementRequest settlementRequest) {

        logger.error("[eureka-client-ppg-user] settlement error;{}", settlementRequest.getUserId());

        return CommonResponse.errorResponse(
                -1,
                "request error",
                Boolean.FALSE
        );
    }
}
