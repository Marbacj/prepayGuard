package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.feign.UserServiceFeign;
import com.mapoh.ppg.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/19 22:43
 */

@Component
public class ContractFeignHystrix implements ContractServiceFeign {

    public static Logger logger = LoggerFactory.getLogger(ContractFeignHystrix.class);

    @Override
    public BigDecimal getAmount(Long contractId) {
        logger.error("[eureka-client-ppg-client] getAmount request error:{}", contractId);

        return null;
    }

    @Override
    public Boolean validContract(Long contractId) {
        logger.error("[eureka-client-ppg-client] validContract request error:{}", contractId);
        return false;
    }

    @Override
    public BigDecimal getUnitAmount(@RequestParam Long contractId){
        logger.warn("[eureka-client-ppg-contract]:getUnitAmount error");
        return BigDecimal.ZERO;
    }
}
