package com.mapoh.ppg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mabohv
 * @date 2025/2/5 23:02
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractScheduledRequest implements Serializable {
    private Long contractId;
    private Long merchantId;
}
