package com.mapoh.ppg.service;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/7 21:15
 */

public interface MerchantService {

    Boolean addAmountByTransfer(Long merchantId, BigDecimal transferAmount);
}
