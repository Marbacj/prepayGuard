package com.mapoh.ppg.feign;

import com.mapoh.ppg.dto.NotificationRequest;
import com.mapoh.ppg.feign.hystrix.NotificationHystrix;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author mabohv
 * @date 2025/2/17 10:31
 */

@FeignClient(value = "eureka-client-ppg-notification",
fallback = NotificationHystrix.class)
public interface NotificationFeign {

    @PostMapping("ppg-notification/notification/sendMessage")
    CommonResponse<Void> sendNotification(@RequestBody NotificationRequest notificationRequest);
}
