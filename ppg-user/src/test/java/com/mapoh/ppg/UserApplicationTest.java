package com.mapoh.ppg;

import com.mapoh.ppg.controller.UserController;
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
        User user = new User();
        user.setUserName("testadfsa");
        user.setPassword("123456");
        user.setEmail("test@test.com");
        user.setAccountName("tes111t");
        user.setIdCardNumber("wsdsdfsf");
        user.setPhoneNumber("123sdf124");

        jwtService.register(user);
        // 使用 jwtService 测试逻辑
        System.out.println("Test register logic");
    }

    @Test
    public void testLogin() {
        String name = "test";
        String password = "123456";

        System.out.println(jwtService.login(name,password));
    }

    @Test
    public void testModifyUserinfo(){
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        long id = 1;

        userInfoRequest.setAccountName("modified");
        userInfoRequest.setPassword("888888");
        userInfoRequest.setEmail("test@example.com");
        userInfoRequest.setPhoneNumber("12345678901");

        System.out.println(userService.modifyUserInfo(userInfoRequest, id));
    }
}