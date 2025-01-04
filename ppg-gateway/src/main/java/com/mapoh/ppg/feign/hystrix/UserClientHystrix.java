package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.feign.UserClientFeign;
import com.mapoh.ppg.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author mabohv
 * @date 2024/12/27 12:37
 */
@Deprecated
@Component
public class UserClientHystrix implements UserClientFeign {

    public static Logger logger = LoggerFactory.getLogger(UserClientHystrix.class);

    @Override
    public CommonResponse<Boolean> checkJwt(String token) {
        logger.error("checkJwt error");
        return CommonResponse.errorResponse(-1, "[eureka-client-coupon-template] request error", null);
    }
}
