package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.dto.RefundRequest;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/14 21:14
 */

public interface PaymentService {

    Boolean payInBalance(BalancePaymentRequest balancePaymentRequest);


    Boolean refundBalance(RefundRequest refundRequest);

    BigDecimal getTotalFee(Long contractId);

    Boolean isProccessed(Long transactionId);
}
