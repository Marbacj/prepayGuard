package com.mapoh.ppg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2024/12/27 18:41
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequest {
    private String password;
    private String phoneNumber;
    private String email;
    private String accountName;
}
