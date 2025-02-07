package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.feign.MerchantServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/7 13:36
 */

@Component
public class MerchantServiceHystrix implements MerchantServiceFeign {

    public static Logger logger = LoggerFactory.getLogger(MerchantServiceHystrix.class);

    @Override
    public Boolean receiveTransferAccount(Long merchantId, BigDecimal transferAmount){
        logger.error("[eureka-client-ppg-merchant]: receiveTransferAccount occurs error");
        return false;
    }
}
