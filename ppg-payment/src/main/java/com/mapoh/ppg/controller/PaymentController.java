package com.mapoh.ppg.controller;

import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.dto.PaymentRequest;
import com.mapoh.ppg.service.PaymentService;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.OnlinePaymentResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mabohv
 * @date 2025/1/14 20:47
 */

@RestController
public class PaymentController {

    PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay/balance")
    public CommonResponse<Boolean> payInBalance(@RequestBody BalancePaymentRequest balancePaymentRequest) {
        Long contractId = balancePaymentRequest.getContractId();
        Long userId = balancePaymentRequest.getUserId();
        Status status = balancePaymentRequest.getStatus();

        if(userId == null || contractId == null || status == null) {
            return CommonResponse.successResponse(false);
        }

        return CommonResponse.successResponse(paymentService.payInBalance(balancePaymentRequest));
    }
}
