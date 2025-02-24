package com.mapoh.ppg.dto;

import com.mapoh.ppg.constants.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mabohv
 * @date 2025/1/19 20:52
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalancePaymentRequest {
    private Long userId;

    private Long contractId;

//    private Status status;

}
