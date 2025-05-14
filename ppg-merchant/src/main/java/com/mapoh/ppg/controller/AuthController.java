package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.LoginRequest;
import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.dto.payment.TransferRequest;
import com.mapoh.ppg.entity.Merchant;
import com.mapoh.ppg.service.JwtService;
import com.mapoh.ppg.service.MerchantService;
import com.mapoh.ppg.vo.CommonResponse;
import javassist.NotFoundException;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author mabohv
 * @date 2024/12/30 10:34
 */

@RestController
@CrossOrigin
public class AuthController {

    public final JwtService jwtService;

    public final MerchantService merchantService;

    @Autowired
    public  AuthController(JwtService jwtService, MerchantService merchantService) {
        this.jwtService = jwtService;
        this.merchantService = merchantService;
    }

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @PostMapping("/register")
    public CommonResponse<Boolean> register(@RequestBody RegisterRequest registerRequest) {
        return CommonResponse.successResponse(jwtService.register(registerRequest));
    }

    /**
     *
     *req{
     *     username
     *     password
     *}
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public CommonResponse<String> login(@RequestBody JSONObject loginRequest) {
        String username = loginRequest.getString("userName");
        String password = loginRequest.getString("password");

        LoginRequest loginRequest1 = new LoginRequest(username, password);
        return CommonResponse.successResponse(jwtService.login(loginRequest1));
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

    @PostMapping("/recvtransfer")
    public CommonResponse<Boolean> receiveTransferAccount(@RequestBody TransferRequest transferRequest) {
        Long merchantId = transferRequest.getMerchantId();
        BigDecimal transferAmount = transferRequest.getTransferAmount();
        if(transferAmount.compareTo(BigDecimal.ZERO) == 0 || merchantId == null){
            return CommonResponse.successResponse(Boolean.FALSE);
        }
        return CommonResponse.successResponse(merchantService.addAmountByTransfer(merchantId, transferAmount));
    }

    /**
     *
     * @param transferRequest
     * @return
     * {
     *     "merchantId" :
     *     "transferAmount":
     * }
     */
    @PostMapping("/updateBalanceWithCAS")
    public CommonResponse<Boolean> updateBalanceWithCAS(@RequestBody JSONObject transferRequest) throws NotFoundException, InterruptedException {
        Long merchantId = transferRequest.getLong("merchantId");
        BigDecimal transferAmount = transferRequest.getBigDecimal("transferAmount");

        return CommonResponse.successResponse(merchantService.transferWithRetry(merchantId, transferAmount));
    }


    /**
     * req{
     *     String: merchantName
     * }
     * response{
     *         @Id
     *     @GeneratedValue(strategy = GenerationType.IDENTITY)
     *     @Column(name = "merchant_id", nullable = false)
     *     private Long id;
     *
     *     @Column(name = "merchant_name", nullable = false, unique = true)
     *     private String merchantName;
     *
     *     @Column(name = "password", nullable = false)
     *     private String password;
     *
     *     @Column(name = "id_card_number", nullable = false, unique = true)
     *     private String idCardNumber;
     *
     *     @Column(name = "phone_number", nullable = false)
     *     private String phoneNumber;
     *
     *     @Column(name = "amount")
     *     private BigDecimal amount = BigDecimal.ZERO;
     *
     *     @Column(name = "created_at", updatable = false)
     *     @CreationTimestamp
     *     private Timestamp createdAt;
     *
     *     @Column(name = "version", nullable = false)
     *     private Integer version;
     * }
     * @param merchantName
     * @return
     */
    @GetMapping("/getMerchantInfo/{merchantName}")
    public CommonResponse<Merchant> getMerchantId(@PathVariable("merchantName") String merchantName) {
        return CommonResponse.successResponse(merchantService.getMerchantIdByMerchantName(merchantName));
    }

    @GetMapping("/income/today/{merchantId}")
    public CommonResponse<Double> getTodayIncome(@PathVariable("merchantId") Long merchantId) {
        return CommonResponse.successResponse(merchantService.getIncomeByMerchantId(merchantId));
    }

    @GetMapping("/getMerchantList")
    public CommonResponse<List<JSONObject>> getMerchantList(@RequestBody JSONObject merchantIds) {
        return CommonResponse.successResponse(merchantService.getMerchantList(merchantIds));
    }
}
