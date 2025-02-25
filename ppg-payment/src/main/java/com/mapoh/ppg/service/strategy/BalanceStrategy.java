package com.mapoh.ppg.service.strategy;

import com.mapoh.ppg.service.PayStrategy;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/24 14:36
 */

//todo: transfer the balance pay to there
public class BalanceStrategy implements PayStrategy {
    @Override
    public boolean pay(BigDecimal amount) {
        return true;
    }
}
