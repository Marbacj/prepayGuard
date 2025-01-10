package com.mapoh.ppg.feign;

/**
 * @author mabohv
 * @date 2025/1/9 10:04
 * contractTemplateFeign
 */

import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "eureka-client-contract-template")
public interface ContractTemplateFeign {

    @GetMapping(value = "/contract-template/getTemplate")
    public CommonResponse<ContractTemplate> getContractTemplate(Integer templateId);

}
