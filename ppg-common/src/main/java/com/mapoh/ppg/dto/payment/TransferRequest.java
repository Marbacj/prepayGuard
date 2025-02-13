package com.mapoh.ppg.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/10 13:28
 * 给商家转账dto
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private Long merchantId;
    private BigDecimal transferAmount;
}
