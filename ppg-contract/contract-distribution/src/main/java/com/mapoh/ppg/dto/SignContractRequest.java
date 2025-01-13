package com.mapoh.ppg.dto;

import com.mapoh.ppg.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mabohv
 * @date 2025/1/13 00:09
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignContractRequest {

    private Long contractId;

    private Long signerId;

    private Role role;
}
