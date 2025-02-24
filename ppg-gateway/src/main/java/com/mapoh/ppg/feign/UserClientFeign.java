package com.mapoh.ppg.feign;

import com.mapoh.ppg.feign.hystrix.UserClientHystrix;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mabohv
 * @date 2024/12/27 12:35
 */

@FeignClient(value = "eureka-client-ppg-user",
        fallback = UserClientHystrix.class, primary = false)
public interface UserClientFeign {

    @RequestMapping(value = "/ppg-user/checkJwt", method = RequestMethod.POST)
    CommonResponse<Boolean> checkJwt(@RequestParam("token") String token);

}
