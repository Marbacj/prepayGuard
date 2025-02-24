package com.mapoh.ppg.feign.hystrix;

import com.mapoh.ppg.dto.NotificationRequest;
import com.mapoh.ppg.feign.NotificationFeign;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.stereotype.Component;

/**
 * @author mabohv
 * @date 2025/2/19 08:05
 */

@Component
public class NotificationHystrix implements NotificationFeign {


    @Override
    public CommonResponse<Void> sendNotification(NotificationRequest notificationRequest) {
        return new CommonResponse<>(
                -1,
                "[eureka-client-ppg-notification] occurs error: ",
                null
        );
    }
}
