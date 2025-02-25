package com.mapoh.ppg.service;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/24 14:33
 */

public interface PayStrategy {
    boolean pay(BigDecimal amount);
}
