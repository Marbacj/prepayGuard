package com.mapoh.ppg.feign;

/**
 * @author mabohv
 * @date 2025/1/9 10:04
 * contractTemplateFeign
 */

import com.mapoh.ppg.feign.hystrix.ContractTemplateHystrix;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractTemplateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "eureka-client-contract-template",
        fallback = ContractTemplateHystrix.class)
public interface ContractTemplateFeign {

    @GetMapping("/ppg-contract/template/getTemplate/{templateId}")
    CommonResponse<ContractTemplateResponse> getTemplateById(@PathVariable("templateId") Integer templateId);

}