package com.mapoh.ppg.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author mabohv
 * @date 2025/2/14 20:18
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractVo {

    private Long userId;

    private Long merchantId;

    private BigDecimal unitAmount;

    private Integer totalUnits;

    private BigDecimal totalAmount;

//    private ValidityUnit validityUnit;
//
//    private enum ValidityUnit{
//        WEEK,
//        MONTH,
//        YEAR
//    }
    private Timestamp unitTime;

    private Integer validityPeriod;
}
