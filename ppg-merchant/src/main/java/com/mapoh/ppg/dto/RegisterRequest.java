package com.mapoh.ppg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mabohv
 * @date 2024/12/30 10:36
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String userName;
    private String password;
    private String idCardNumber;
    private String phoneNumber;
}
