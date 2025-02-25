package com.mapoh.ppg.service.strategy;

import com.mapoh.ppg.service.PayStrategy;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/24 15:33
 */

public class PaypalStrategy implements PayStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        return true;
    }
}
