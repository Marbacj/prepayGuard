package com.mapoh.ppg.vo;

import com.mapoh.ppg.constants.ActivationMethod;
import com.mapoh.ppg.constants.ValidityUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/10 18:32
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractTemplateResponse {
    private String templateName;
    private BigDecimal unitAmount;
    private Integer validityPeriod;
    private ValidityUnit validityUnit;
    private ActivationMethod activationMethod;
    private Boolean refundable;
}
