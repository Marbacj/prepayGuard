package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.LoginRequest;
import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.service.JwtService;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实现用户注册，用户登陆
 * todo: 重名检测
 * @author mabohv
 * @date 2024/12/25 16:12
 */

@RestController("/user")
@CrossOrigin
public class AuthController {

    private final JwtService jwtService;

    @Autowired
    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @RequestMapping({"/","/index"})
    public String index() {
        return "index";
    }

    /**
     * 1. 注册成功
     * 2. 信息重复，注册失败
     * @param user
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public CommonResponse<String> register(@RequestBody RegisterRequest registerRequest) {
        boolean registerStatus = jwtService.register(registerRequest);
        if(registerStatus) {
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.errorResponse(401, "Invalid Info", null);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResponse<String> login(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getUserName();
        String password = loginRequest.getPassword();

        String token = jwtService.login(username, password);
        if(token == null) {
            return CommonResponse.errorResponse(401, "Invalid userName or password", null);
        }
        return CommonResponse.successResponse(token);
    }

    @RequestMapping(value = "/checkJwt")
    public CommonResponse<Boolean> checkJwt(@RequestParam String token) {
        return CommonResponse.successResponse(jwtService.checkJwt(token));
    }

    @RequestMapping(value = "refreshJwt")
    public CommonResponse<String> refreshJwt(String token) {
        return CommonResponse.successResponse(jwtService.refreshJwt(token));
    }

    @RequestMapping
    public CommonResponse<String> invalid(String token) {
        jwtService.invalidJwt(token);
        return CommonResponse.successResponse(null);
    }
}
