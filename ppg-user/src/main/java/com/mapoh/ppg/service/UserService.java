package com.mapoh.ppg.service;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.UserInfoRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mabohv
 * @date 2024/12/27 18:33
 */

public interface UserService {


    BigDecimal getUserBalance(Long userId);

    boolean settlement(Long userId, BigDecimal amount);

    JSONObject getUserDetailsByUsername(String username);

//    Boolean modifyUserInfo(String accountName, String email, String phone, String password);

    Boolean rechargeInBalance(String accountName, BigDecimal amount);

    Boolean modifyUserInfo(Long id, String accountName, String email, String phone, String password);

    List<JSONObject> getAllUsers();
}
