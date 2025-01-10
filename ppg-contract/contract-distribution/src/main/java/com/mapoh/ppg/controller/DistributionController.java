package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.service.DistributionService;
import com.mapoh.ppg.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mabohv
 * @date 2025/1/10 19:27
 * <ol>
 *     <li>用户创建合同</li>
 *     <li>商家签署合同</li>
 *     <li>用户签署合同</li>
 *     <li>合同生效</li>
 * </ol>
 *
 */

@RestController
public class DistributionController {

    public static Logger logger = LoggerFactory.getLogger(DistributionController.class);


    public  DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @PostMapping("/create")
    public CommonResponse<String> createContract(@RequestBody CreateContractRequest createContractRequest){
        if(createContractRequest.getUserId() == null
            || createContractRequest.getTemplateId() == null){
            logger.warn("the userId is null or contractId is null");
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(distributionService.createContract(createContractRequest));
    }
}
