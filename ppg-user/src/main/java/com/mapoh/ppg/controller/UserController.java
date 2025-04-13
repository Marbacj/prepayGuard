package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.UserInfoRequest;
import com.mapoh.ppg.dto.payment.SettlementRequest;
import com.mapoh.ppg.service.UserService;
import com.mapoh.ppg.vo.CommonResponse;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Resource
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/balance/{userId}")
    public CommonResponse<BigDecimal> getBalance(@PathVariable("userId") Long userId) {
        return CommonResponse.successResponse(userService.getUserBalance(userId));
    }


    /**
     *
     * {
     *     id:
     *     email:
     *     phonenumber:
     *     balance:
     * }
     * @param username
     * @return
     */
    @GetMapping("/details/{username}")
    public CommonResponse<JSONObject> getUserDetails(@PathVariable("username") String username) {
        if(username == null) {
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(userService.getUserDetailsByUsername(username));
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


    /**
     * // 更新用户信息接口规范
     * PUT /api/user/profile
     * Request:
     * {
     *   "id":
     *   "accountName": "新用户名",  // 必填
     *   "email": "new@email.com",  // 必填
     *   "phoneNumber": "13812345678",  // 必填
     *   "password": "newPassword"  // 选填（为空时不修改）
     * }
     *
     * Response:
     * {
     *   "code": 200,
     *   "data": {
     *   1 // 更新成功
     *   0 // 更新失败
     *
     * @param userInfoRequest
     * @return
     */
    @PutMapping("/modify")
    public CommonResponse<Boolean> modifyUserInfo(@RequestBody JSONObject userInfoRequest) {
        Long id = userInfoRequest.getLong("id");
        String accountName = userInfoRequest.getString("accountName");
        String email = userInfoRequest.getString("email");
        String phone = userInfoRequest.getString("phoneNumber");
        String password = userInfoRequest.getString("password");

        return CommonResponse.successResponse(userService.modifyUserInfo(id, accountName, email, phone, password));
    }

    /**
     * req
     * {
     *   "accountName": "zhangsan",
     *   "amount": 100.0,
     * }
     * response
     * {
     *     code:
     *     message:
     *     data:{
     *         false
     *         true
     *     }
     * }
     */
    @PutMapping("/balance/recharge")
    public CommonResponse<Boolean> rechargeToBalance(@RequestBody JSONObject rechargeRequest) {
        String accountName = rechargeRequest.getString("accountName");
        BigDecimal amount = rechargeRequest.getBigDecimal("amount");
        if(accountName == null) {
            final String message = "recharge failed";
            return CommonResponse.successResponse(Boolean.FALSE);
        }
        return CommonResponse.successResponse(userService.rechargeInBalance(accountName, amount));
    }
}
