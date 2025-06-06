package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.dto.SignContractRequest;
import com.mapoh.ppg.service.DistributionService;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
@CrossOrigin
public class DistributionController {

    public static Logger logger = LoggerFactory.getLogger(DistributionController.class);


    public DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    /**
     * req{
     *     "templateId"
     *     "userId"
     *     "merchantId"
     *     "totalUnits"
     *     "totalAmount"
     *     "activationDate"
     * }
     * response{
     *     "contractCode":
     * }
     * @param createContractRequest
     * @return
     */
    @PostMapping("/create/byTemplate")
    public CommonResponse<Long> createContract(@RequestBody CreateContractRequest createContractRequest) {
        if (createContractRequest.getUserId() == null
                || createContractRequest.getTemplateId() == null) {
            logger.warn("the userId is null or contractId is null");
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(distributionService.createContract(createContractRequest));
    }


    /**
     * @param signContractRequest
     * @return
     */
    @PostMapping("/sign")
//    @GlobalTransactional(name = "contract-sign-tx", rollbackFor = Exception.class)
    public CommonResponse<String> signContract(@RequestBody SignContractRequest signContractRequest) {

        String role = signContractRequest.getRole().toString();
        Long contractId = signContractRequest.getContractId();
        Long signerId = signContractRequest.getSignerId();

        if (role == null || contractId == null || signerId == null) {
            logger.warn("the role and contractId is null");
            return CommonResponse.successResponse(null);
        }

        return CommonResponse.successResponse(distributionService.signContract(signContractRequest));
    }

    /**
     * 得到合同总的价格
     *
     * @param contractId
     * @return
     */
    @GetMapping("/amount/{contractId}")
    public CommonResponse<BigDecimal> getAmount(@PathVariable("contractId") Long contractId) {
        return CommonResponse.successResponse(distributionService.getAmount(contractId));
    }

    /**
     * 使合同进行生效
     *
     * @param contractId
     * @return
     */
    @PostMapping("/validContract/{contractId}")
    public CommonResponse<Boolean> validContract(@PathVariable("contractId") Long contractId) {
        if (contractId == null) {
            logger.warn("the contractId is null");
            return CommonResponse.successResponse(Boolean.FALSE);
        }
        return CommonResponse.successResponse(distributionService.validateContract(contractId));
    }

    /**
     * 得到单期价格
     *
     * @param contractId
     * @return
     */
    @GetMapping("/unitamount/{contractId}")
    public CommonResponse<BigDecimal> getUnitAmount(@PathVariable("contractId") Long contractId) {
        if (contractId == null) {
            logger.warn("the contractId is null");
            return CommonResponse.successResponse(BigDecimal.ZERO);
        }
        return CommonResponse.successResponse(distributionService.getUnitAmount(contractId));
    }

    @GetMapping("/getContractId/{contractId}")
    public CommonResponse<Long> getMerchantId(@PathVariable("contractId") Long contractId) {
        if (contractId == null) {
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(distributionService.getMerchantId(contractId));
    }

    @GetMapping("/getContractVo/{contractId}")
    public CommonResponse<ContractVo> getContractVo(@PathVariable("contractId") Long contractId) {
        if (contractId == null) {
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(distributionService.getContractVoByContractId(contractId));
    }

    /**
     *{
     *     "contractId"
     *     "userID"
     *     "merchantID"
     *     "transffered_units"
     *     "
     *}
     *
     */
//    @GetMapping("/getContract/refund")
////    public CommonResponse<JSONObject> getRefundContractInfo(){
////
////    }
    //查询合同
    @GetMapping("/pendingOrder/today/{merchantId}")
    public CommonResponse<Integer> getTodayPendingOrder(@PathVariable("merchantId") Long merchantId) {
        return CommonResponse.successResponse(distributionService.getPendingOrderByMerchantId(merchantId));
    }

    @GetMapping("getNewCustomer/{merchantId}")
    public CommonResponse<Integer> getNewCustomer(@PathVariable("merchantId") Long merchantId){
        return CommonResponse.successResponse(distributionService.getNewCustomer(merchantId));
    }

    @GetMapping("/getContractList/{merchantId}")
    public CommonResponse<List<JSONObject>> getContractList(@PathVariable("merchantId") Long merchantId) {
        return CommonResponse.successResponse(distributionService.getContractListByMerchantId(merchantId));
    }

    @PostMapping("/delete/{contractId}")
    public CommonResponse<String> deleteContract(@PathVariable("contractId") Long ContractId){
        if(ContractId == null)
            return CommonResponse.successResponse("contractId is null");
        return CommonResponse.successResponse(distributionService.deleteContract(ContractId));
    }

    /**
     * {
     *     "userId"
     *     "merchantId"
     * }
     *
     * @param request
     * @return
     */
    @GetMapping("/userhistory/contractList/{userId}")
    public CommonResponse<List<JSONObject>> getUserHistoryContractList(@PathVariable("userId") Long userId){
        return CommonResponse.successResponse(distributionService.getUserHistoryContractList(userId));
    }

    /**
     * getUnpayedContractList
     * @param userId
     * @return
     */
    @GetMapping("/getUnpayed/contractList/{userId}")
    public CommonResponse<List<JSONObject>> getUnpayedContractList(@PathVariable("userId") Long userId){
        return CommonResponse.successResponse(distributionService.getUnpayedContractList(userId));
    }


    @PostMapping("refund/updateStatus/{contractId}")
    public CommonResponse<Boolean> updateRefundStatus(@PathVariable("contractId") Long contractId){
        return CommonResponse.successResponse(distributionService.changeStatusToRefund(contractId));
    }

}