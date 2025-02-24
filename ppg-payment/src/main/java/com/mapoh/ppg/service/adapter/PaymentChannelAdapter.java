package com.mapoh.ppg.service.adapter;

/**
 * @author mabohv
 * @date 2025/1/14 21:15
 */

public interface PaymentChannelAdapter {

    public void pay(double amount);

    public void pay(Long userId, Long contractId);
}
