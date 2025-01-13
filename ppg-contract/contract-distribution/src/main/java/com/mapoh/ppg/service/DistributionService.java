package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.dto.SignContractRequest;

/**
 * @author mabohv
 * @date 2025/1/10 19:46
 */

public interface DistributionService {

    String createContract(CreateContractRequest createContractRequest);

    String signContract(SignContractRequest signContractRequest);
}
