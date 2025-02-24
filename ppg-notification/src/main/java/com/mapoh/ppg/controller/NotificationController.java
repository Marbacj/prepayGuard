package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.NotificationRequest;
import com.mapoh.ppg.service.NotificationService;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mabohv
 * @date 2025/2/17 10:03
 */

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/sendMessage")
    CommonResponse<Void> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationService.SendNotificationToUser(notificationRequest);
        return CommonResponse.successResponse();
    }
}
