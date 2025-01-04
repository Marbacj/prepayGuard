package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.LoginRequest;
import com.mapoh.ppg.dto.RegisterRequest;

/**
 * @author mabohv
 * @date 2024/12/30 10:43
 */

public interface JwtService {

    public boolean register(RegisterRequest registerRequest);

    public String login(LoginRequest loginRequest);

    public boolean checkJwt(String token);
}
