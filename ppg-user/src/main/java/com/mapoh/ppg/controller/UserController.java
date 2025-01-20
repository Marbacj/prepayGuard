package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.UserInfoRequest;
import com.mapoh.ppg.entity.User;
import com.mapoh.ppg.service.UserService;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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


    @GetMapping("/balance")
    public BigDecimal getBalance(Long userId) {
        return userService.getUserBalance(userId);
    }

    @PostMapping("/settlement")
    public Boolean settlement(Long userId, BigDecimal amount) {
        if(userId == null) {
            return Boolean.FALSE;
        }

        return userService.settlement(userId, amount);
    }
}
