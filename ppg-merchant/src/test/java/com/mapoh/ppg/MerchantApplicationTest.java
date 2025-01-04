package com.mapoh.ppg;

import com.mapoh.ppg.dto.LoginRequest;
import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.service.JwtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mabohv
 * @date 2024/12/30 13:00
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MerchantApplicationTest {

    @Autowired
    JwtService jwtService;

    @Test
    public void merchantRegister(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName("test");
        registerRequest.setPassword("123456");
        registerRequest.setPhoneNumber("12324234");
        registerRequest.setIdCardNumber("aaaaaa");

        System.out.println(jwtService.register(registerRequest));
    }
    @Test
    public void merchantLoginTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMerchantName("mabohv");
        loginRequest.setPassword("mabohv");

        System.out.println(jwtService.login(loginRequest));
    }
}
