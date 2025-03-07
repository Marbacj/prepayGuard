package com.mapoh.ppg.service;

import javassist.NotFoundException;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/7 21:15
 */

public interface MerchantService {

    Boolean addAmountByTransfer(Long merchantId, BigDecimal transferAmount);

    Boolean transferWithRetry(Long merchantId, BigDecimal transferAmount) throws InterruptedException, NotFoundException;
}
