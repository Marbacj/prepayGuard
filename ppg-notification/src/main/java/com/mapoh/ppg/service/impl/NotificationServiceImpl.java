package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.dto.NotificationRequest;
import com.mapoh.ppg.server.WebSocketServer;
import com.mapoh.ppg.service.NotificationService;
import org.springframework.stereotype.Service;

/**
 * @author mabohv
 * @date 2025/2/17 10:11
 */

@Service
public class NotificationServiceImpl implements NotificationService {

    /**
     * todo: add the user offline can also read the info
     * @param notificationRequest
     */
    @Override
    public void SendNotificationToUser(NotificationRequest notificationRequest) {
        Long userId = notificationRequest.getUserId();
        String message = notificationRequest.getMessage();
        WebSocketServer.sendMessage(userId.toString(), message);
    }
}
