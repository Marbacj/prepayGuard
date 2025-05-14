package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractVo;
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
    public CommonResponse<BigDecimal> getAmount(Long contractId) {
        logger.error("[eureka-client-ppg-contract] occurs error:{}", contractId);
        return CommonResponse.errorResponse(
                -1,
                "invoke error",
                null
        );
    }

    @Override
    public CommonResponse<Boolean> validContract(Long contractId) {
        logger.error("[eureka-client-ppg-client] validContract request error:{}", contractId);
        return CommonResponse.errorResponse(
                -1,
                " ",
                Boolean.FALSE
        );
    }

    @Override
    public CommonResponse<BigDecimal> getUnitAmount(@RequestParam Long contractId){
        logger.warn("[eureka-client-ppg-contract]:getUnitAmount error");
        return CommonResponse.errorResponse(
                -1,
                " ",
                BigDecimal.ZERO
        );
    }

    @Override
    public CommonResponse<Long> getMerchantId(Long contractId) {
        logger.warn("[eureka-client-ppg-contract]:getUnitAmount error:{}", contractId);

        return CommonResponse.errorResponse(
                -1,
                "",
                null
        );
    }

    @Override
    public CommonResponse<ContractVo> getContractVo(Long contractId) {
        logger.warn("[eureka-client-ppg-contract]:getContractVo error:{}", contractId);

        return CommonResponse.errorResponse(
                -1,
                "",
                null
        );
    }

    @Override
    public CommonResponse<Boolean> updateRefundStatus(Long contractId) {
        logger.warn("[eureka-client-ppg-contract]:updateRefundStatus error:{}", contractId);
        return CommonResponse.errorResponse(
                -1,
                "",
                null
        );
    }
}
