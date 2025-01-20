package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.feign.UserServiceFeign;
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
    public BigDecimal getBalance(Long userId) {

        logger.error("[eureka-client-ppg-user] getBalance error;{}", userId);
        return null;
    }

    @Override
    public Boolean settlement(Long userId, BigDecimal amount) {

        logger.error("[eureka-client-ppg-user] settlement error;{}", userId);

        return null;
    }
}
