package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.LoginRequest;
import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.service.JwtService;
import com.mapoh.ppg.util.JwtUtil;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.entity.User;
import io.netty.handler.codec.json.JsonObjectDecoder;
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
     *
     * 成功注册
     * todo：信息重复
     * @param registerRequest
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


    /**
     *
     * {
     *     "userName:
     *     "token
     * }
     * @param loginRequest
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResponse<JSONObject> login(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getUserName();
        String password = loginRequest.getPassword();

        String token = jwtService.login(username, password);
        if(token == null) {
            return CommonResponse.errorResponse(401, "Invalid userName or password", null);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("userName", username);
        return CommonResponse.successResponse(jsonObject);
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
