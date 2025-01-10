package com.mapoh.ppg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author mabohv
 * @date 2025/1/10 19:36
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractRequest {
    private String templateId;
    private String userId;
    private String merchantId;
    private Integer totalUnits;
    private BigDecimal totalAmount;
//    private LocalDate activationDate;
}
