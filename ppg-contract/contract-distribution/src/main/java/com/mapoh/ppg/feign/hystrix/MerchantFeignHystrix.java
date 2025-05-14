package com.mapoh.ppg.feign.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.feign.MerchantFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author mabohv
 * @date 2025/4/18 22:16
 */

@Component
public class MerchantFeignHystrix implements MerchantFeign {
    public static Logger logger = LoggerFactory.getLogger(MerchantFeignHystrix.class);

    @Override
    public List<JSONObject> getMerchantDetails(List<Long> userIds) {
        return Collections.emptyList();
    }
}
