package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.feign.UserServiceFeign;
import com.mapoh.ppg.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/14 21:20
 */

@Service
public class PaymentServiceImpl implements PaymentService {

    public static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    ContractServiceFeign contractServiceFeign;

    UserServiceFeign userServiceFeign;

    PaymentServiceImpl(ContractServiceFeign contractServiceFeign,
                       UserServiceFeign userServiceFeign) {
        this.contractServiceFeign = contractServiceFeign;
        this.userServiceFeign = userServiceFeign;
    }

    @Override
    public Boolean payInBalance(BalancePaymentRequest balancePaymentRequest) {
        Long userId = balancePaymentRequest.getUserId();
        Long contractId = balancePaymentRequest.getContractId();
        Status status = balancePaymentRequest.getStatus();

        if(userId == null || contractId == null || status == null) {
            logger.info("It's missed excepted param");
        }
        BigDecimal amount = contractServiceFeign.getAmount(contractId);

        BigDecimal balance = userServiceFeign.getBalance(userId);

        if(balance.compareTo(amount) < 0) {
            logger.error("the user balance is not enough:{}", balance);
            return false;
        }

        try {
            userServiceFeign.settlement(userId, amount);
            contractServiceFeign.validContract(contractId);
            return true;
        }catch (Exception e) {
            logger.error("change status or settlement error:{}", e.getMessage());
            return false;
        }
    }
}
