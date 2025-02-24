package com.mapoh.ppg.controller;

import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.dto.RefundRequest;
import com.mapoh.ppg.service.PaymentService;
import com.mapoh.ppg.service.adapter.PaymentChannelAdapter;
import com.mapoh.ppg.service.adapter.PaypalAdapter;
import com.mapoh.ppg.service.impl.PaypalPayment;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.OnlinePaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mabohv
 * @date 2025/1/14 20:47
 */

@RestController
public class PaymentController {

    PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay/balance")
    public CommonResponse<Boolean> payInBalance(@RequestBody BalancePaymentRequest balancePaymentRequest) {
        Long contractId = balancePaymentRequest.getContractId();
        Long userId = balancePaymentRequest.getUserId();

        if(userId == null || contractId == null) {
            return CommonResponse.successResponse(false);
        }

        return CommonResponse.successResponse(paymentService.payInBalance(balancePaymentRequest));
    }

    @PostMapping("pay/paypal")
    public CommonResponse<Boolean> payInpaypal(@RequestBody BalancePaymentRequest balancePaymentRequest) {
        Long contractId = balancePaymentRequest.getContractId();
        Long userId = balancePaymentRequest.getUserId();

        if(userId == null || contractId == null) {
            return CommonResponse.successResponse(false);
        }
        PaymentChannelAdapter payment = new PaypalAdapter(new PaypalPayment());
        payment.pay(contractId, userId);
        return CommonResponse.successResponse(true);
    }

    @PostMapping("/refund")
    public CommonResponse<Boolean> refund(@RequestBody RefundRequest refundRequest) {
        if(refundRequest == null || refundRequest.getContractId() == null || refundRequest.getUserId() == null) {
            return CommonResponse.successResponse(false);
        }
        return CommonResponse.successResponse(paymentService.refundBalance(refundRequest));
    }
}
