package com.mapoh.ppg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mabohv
 * @date 2025/2/15 14:17
 */

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {

    private Long userId;

    private Long contractId;

    private String reason;
}
