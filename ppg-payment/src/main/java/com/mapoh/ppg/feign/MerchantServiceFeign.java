package com.mapoh.ppg.feign;

import com.mapoh.ppg.feign.hystrix.MerchantServiceHystrix;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/7 10:06
 */

@FeignClient(value = "eureka-client-ppg-merchant",
fallback = MerchantServiceHystrix.class)
public interface MerchantServiceFeign {

    @PostMapping("/recvtransfer")
    public Boolean receiveTransferAccount(Long merchantId, BigDecimal transferAmount);

}
