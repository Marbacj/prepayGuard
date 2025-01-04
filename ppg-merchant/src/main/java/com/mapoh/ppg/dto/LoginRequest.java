package com.mapoh.ppg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mabohv
 * @date 2024/12/30 11:05
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String merchantName;
    private String password;
}
