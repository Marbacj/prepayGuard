package com.mapoh.ppg.feign;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.payment.TransferRequest;
import com.mapoh.ppg.feign.hystrix.MerchantServiceHystrix;
import com.mapoh.ppg.vo.CommonResponse;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/7 10:06
 */

@FeignClient(value = "eureka-client-ppg-merchant",
fallback = MerchantServiceHystrix.class)
public interface MerchantServiceFeign {

    @PostMapping("ppg-merchant/recvtransfer")
    public CommonResponse<Boolean> receiveTransferAccount(@RequestBody TransferRequest transferRequest);

    @PostMapping("/updateBalanceWithCAS")
    public CommonResponse<Boolean> updateBalanceWithCAS(@RequestBody JSONObject transferRequest);
}
