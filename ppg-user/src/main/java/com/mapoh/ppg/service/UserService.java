package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.UserInfoRequest;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2024/12/27 18:33
 */

public interface UserService {

    boolean modifyUserInfo(UserInfoRequest userInfoRequest, Long id);

    BigDecimal getUserBalance(Long userId);

    boolean settlement(Long userId, BigDecimal amount);
}
