package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.UserInfoRequest;
import com.mapoh.ppg.dto.payment.SettlementRequest;
import com.mapoh.ppg.service.UserService;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 实现用户修改信息
 * @author mabohv
 * @date 2024/12/27 18:34
 */


@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public CommonResponse<Boolean> modify(@RequestBody UserInfoRequest userInfoRequest, Long id) {
        return CommonResponse.successResponse(userService.modifyUserInfo(userInfoRequest, id));
    }


    @GetMapping("/balance/{userId}")
    public CommonResponse<BigDecimal> getBalance(@PathVariable("userId") Long userId) {
        return CommonResponse.successResponse(userService.getUserBalance(userId));
    }






    @PostMapping("/settlement")
    public CommonResponse<Boolean> settlement(@RequestBody SettlementRequest settlementRequest) {
        Long userId = settlementRequest.getUserId();
        BigDecimal amount = settlementRequest.getAmount();
        if(userId == null) {
            return CommonResponse.successResponse(Boolean.FALSE);
        }

        return CommonResponse.successResponse(userService.settlement(userId, amount));
    }
}
