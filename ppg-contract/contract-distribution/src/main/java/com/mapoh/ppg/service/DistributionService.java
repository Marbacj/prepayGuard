package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.dto.SignContractRequest;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/10 19:46
 */

public interface DistributionService {

    String createContract(CreateContractRequest createContractRequest);

    String signContract(SignContractRequest signContractRequest);

    BigDecimal getAmount(Long contractId);

    Boolean validateContract(Long contractId);

    BigDecimal getUnitAmount(Long contractId);

    Long getMerchantId(Long contractId);
}
