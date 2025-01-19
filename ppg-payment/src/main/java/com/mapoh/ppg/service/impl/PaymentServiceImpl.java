package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author mabohv
 * @date 2025/1/14 21:20
 */

@Service
public class PaymentServiceImpl implements PaymentService {

    public static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Override
    public Boolean payInBalance(BalancePaymentRequest balancePaymentRequest) {
        Long userId = balancePaymentRequest.getUserId();
        Long contractId = balancePaymentRequest.getContractId();
        Status status = balancePaymentRequest.getStatus();

        if(userId == null || contractId == null || status == null) {
            logger.info("It's missed excepted param");
        }



    }
}
