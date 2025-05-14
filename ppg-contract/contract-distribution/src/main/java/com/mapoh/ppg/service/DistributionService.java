package com.mapoh.ppg.service;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.dto.SignContractRequest;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mabohv
 * @date 2025/1/10 19:46
 */

public interface DistributionService {

    Long createContract(CreateContractRequest createContractRequest);

    String signContract(SignContractRequest signContractRequest);

    BigDecimal getAmount(Long contractId);

    Boolean validateContract(Long contractId);

    BigDecimal getUnitAmount(Long contractId);

    Long getMerchantId(Long contractId);

    ContractVo getContractVoByContractId(Long contractId);

    Integer getPendingOrderByMerchantId(Long merchantId);

    Integer getNewCustomer(Long merchantId);

    List<JSONObject> getRelatedCustomers(Long merchantId);

    List<JSONObject> getContractListByMerchantId(Long merchantId);

    String deleteContract(Long contractId);

    List<JSONObject> getUserHistoryContractList(Long userId);

    List<JSONObject> getUnpayedContractList(Long userId);

    Boolean changeStatusToRefund(Long contractId);
}
