package com.mapoh.ppg;

import com.mapoh.ppg.controller.UserController;
import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.dto.UserInfoRequest;
import com.mapoh.ppg.entity.User;
import com.mapoh.ppg.service.JwtService;
import com.mapoh.ppg.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mabohv
 * @date 2024/12/26 22:56
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserApplicationTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    //success
    @Test
    public void testRegister() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName("test");
        registerRequest.setPassword("123456");
        registerRequest.setIdCardNumber("syslog");
        registerRequest.setEmail("test@test.com");
        registerRequest.setPhoneNumber("1388888888");
        registerRequest.setAccountName("fake");

        jwtService.register(registerRequest);
        // 使用 jwtService 测试逻辑
        System.out.println("Test register logic");
    }

    @Test
    public void testLogin() {
        String name = "test";
        String password = "123456";

        System.out.println(jwtService.login(name,password));
    }

//    @Test
//    public void testModifyUserinfo(){
//        UserInfoRequest userInfoRequest = new UserInfoRequest();
//        long id = 1;
//
//        userInfoRequest.setAccountName("modified");
//        userInfoRequest.setPassword("888888");
//        userInfoRequest.setEmail("test@example.com");
//        userInfoRequest.setPhoneNumber("12345678901");
//
//        System.out.println(userService.modifyUserInfo(userInfoRequest, id));
//    }

    @Test
    public void testQueryUserInfo(){
        String username = "user0091";
//        userService.getUserDetailsByUsername(username);
        System.out.println(userService.getUserDetailsByUsername(username));
    }
}