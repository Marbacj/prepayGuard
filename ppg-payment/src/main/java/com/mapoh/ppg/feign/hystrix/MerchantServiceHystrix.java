package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.dto.payment.TransferRequest;
import com.mapoh.ppg.feign.MerchantServiceFeign;
import com.mapoh.ppg.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author mabohv
 * @date 2025/2/7 13:36
 */

@Component
public class MerchantServiceHystrix implements MerchantServiceFeign {

    public static Logger logger = LoggerFactory.getLogger(MerchantServiceHystrix.class);

    @Override
    public CommonResponse<Boolean> receiveTransferAccount(TransferRequest transferRequest){
        logger.error("[eureka-client-ppg-merchant]: receiveTransferAccount occurs error");
        return CommonResponse.errorResponse(
                -1,
                "",
                Boolean.FALSE
        );
    }
}
