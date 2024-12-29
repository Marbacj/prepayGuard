package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.UserInfoRequest;

/**
 * @author mabohv
 * @date 2024/12/27 18:33
 */

public interface UserService {

    public boolean modifyUserInfo(UserInfoRequest userInfoRequest, Long id);
}
