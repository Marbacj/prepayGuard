package com.mapoh.ppg.service.adapter;

import com.mapoh.ppg.service.impl.PaypalPayment;

/**
 * @author mabohv
 * @date 2025/2/22 08:01
 */

public class PaypalAdapter implements PaymentChannelAdapter{

    private PaypalPayment paypalPayment;

    public PaypalAdapter(PaypalPayment paypalPayment) {
        this.paypalPayment = paypalPayment;
    }

    @Override
    public void pay(double amount) {

    }

    @Override
    public void pay(Long userId, Long contractId) {

    }
}
