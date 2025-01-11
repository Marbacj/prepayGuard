package com.mapoh.ppg.feign;

/**
 * @author mabohv
 * @date 2025/1/9 10:04
 * contractTemplateFeign
 */

import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.feign.hystrix.ContractTemplateHystrix;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "eureka-client-contract-template",
        fallback = ContractTemplateHystrix.class)
public interface ContractTemplateFeign {

    @GetMapping(value = "/ppg-contract/template/template")
    CommonResponse<ContractTemplate> getTemplateById(@RequestParam("templateId") Integer templateId);
}
