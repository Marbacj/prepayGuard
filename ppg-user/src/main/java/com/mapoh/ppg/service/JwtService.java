package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.entity.User;

/**
 * @author mabohv
 * @date 2024/12/25 16:13
 */

public interface JwtService {

//    String generateToken(String userId, String secret);

    boolean checkJwt(String token);

    boolean register(RegisterRequest registerRequest);

    String login(String username, String password);

    String refreshJwt(String oldToken);

    void invalidJwt(String token);
}
