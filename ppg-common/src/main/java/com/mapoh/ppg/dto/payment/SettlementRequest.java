package com.mapoh.ppg.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/8 22:13
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementRequest {
    private Long userId;
    private BigDecimal amount;
}
