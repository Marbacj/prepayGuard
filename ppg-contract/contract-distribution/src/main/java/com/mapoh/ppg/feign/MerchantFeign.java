package com.mapoh.ppg.feign;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.feign.hystrix.MerchantFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author mabohv
 * @date 2025/4/18 22:15
 */
@FeignClient(value = "eureka-client-ppg-merchant",
fallback = MerchantFeignHystrix.class)
public interface MerchantFeign {


    @GetMapping("")
    List<JSONObject> getMerchantDetails(List<Long> userIds);
}
