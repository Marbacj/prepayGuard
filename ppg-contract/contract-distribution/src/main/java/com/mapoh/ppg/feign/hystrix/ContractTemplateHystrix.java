package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.feign.ContractTemplateFeign;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractTemplateResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author mabohv
 * @date 2025/1/11 23:09
 */

@Component
public class ContractTemplateHystrix implements ContractTemplateFeign {

    public static Logger logger = LoggerFactory.getLogger(ContractTemplateHystrix.class);

    @Override
    public CommonResponse<ContractTemplateResponse> getTemplateById(Integer templateId) {
        logger.error("[eureka-client-contract-template] getTemplateById" +
                "request error");
        return new CommonResponse<>(
                -1,
                "request error",
                null
        );
    }
}