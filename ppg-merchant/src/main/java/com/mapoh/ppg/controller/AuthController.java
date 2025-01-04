package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.LoginRequest;
import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.service.JwtService;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mabohv
 * @date 2024/12/30 10:34
 */

@RestController("merchant")
@CrossOrigin
public class AuthController {

    public final JwtService jwtService;

    @Autowired
    public  AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @PostMapping("/register")
    public CommonResponse<Boolean> register(@RequestBody RegisterRequest registerRequest) {
        return CommonResponse.successResponse(jwtService.register(registerRequest));
    }

    @PostMapping("/login")
    public CommonResponse<String> login(@RequestBody LoginRequest loginRequest) {
        return CommonResponse.successResponse(jwtService.login(loginRequest));
    }

    @RequestMapping("/checkJwt")
    public CommonResponse<Boolean> checkJwt(String token) {
        if(token == null){
            return CommonResponse.successResponse(false);
        }
        boolean checkResult = jwtService.checkJwt(token);
        if(checkResult){
            return CommonResponse.successResponse(true);
        }
        return CommonResponse.successResponse(false);
    }
}
