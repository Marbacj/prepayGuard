package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.PaymentRequest;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.PaymentResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mabohv
 * @date 2025/1/14 20:47
 */

@RestController
public class PaymentController {

    @GetMapping("/pay")
    public CommonResponse<PaymentResponse> process(@RequestBody PaymentRequest request) {

    }
}
