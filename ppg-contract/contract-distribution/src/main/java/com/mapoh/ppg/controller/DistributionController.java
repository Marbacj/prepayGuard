package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.dto.SignContractRequest;
import com.mapoh.ppg.service.DistributionService;
import com.mapoh.ppg.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    /**
     *
     * @param createContractRequest
     * @return
     */
    @PostMapping("/create")
    public CommonResponse<String> createContract(@RequestBody CreateContractRequest createContractRequest){
        if(createContractRequest.getUserId() == null
            || createContractRequest.getTemplateId() == null){
            logger.warn("the userId is null or contractId is null");
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(distributionService.createContract(createContractRequest));
    }

    /**
     *
     * @param signContractRequest
     * @return
     */
    @PostMapping("/sign")
//    @GlobalTransactional(name = "contract-sign-tx", rollbackFor = Exception.class)
    public CommonResponse<String> signContract(@RequestBody SignContractRequest signContractRequest){

        String role = signContractRequest.getRole().toString();
        Long contractId = signContractRequest.getContractId();
        Long signerId = signContractRequest.getSignerId();

        if(role == null || contractId == null || signerId == null){
            logger.warn("the role and contractId is null");
            return CommonResponse.successResponse(null);
        }

        return CommonResponse.successResponse(distributionService.signContract(signContractRequest));
    }

    @GetMapping("/amount/{contractId}")
    public CommonResponse<BigDecimal> getAmount(@PathVariable("contractId") Long contractId){
        return CommonResponse.successResponse(distributionService.getAmount(contractId));
    }

    @PostMapping("/validContract/{contractId}")
    public CommonResponse<Boolean> validContract(@PathVariable("contractId") Long contractId){
        if(contractId == null){
            logger.warn("the contractId is null");
            return CommonResponse.successResponse(Boolean.FALSE);
        }
        return CommonResponse.successResponse(distributionService.validateContract(contractId));
    }

    @GetMapping("/unitamount/{contractId}")
    public CommonResponse<BigDecimal> getUnitAmount(@PathVariable("contractId") Long contractId){
        if(contractId == null){
            logger.warn("the contractId is null");
            return CommonResponse.successResponse(BigDecimal.ZERO);
        }
        return CommonResponse.successResponse(distributionService.getUnitAmount(contractId));
    }
}
