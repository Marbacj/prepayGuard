package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.NotificationRequest;

/**
 * @author mabohv
 * @date 2025/2/17 10:11
 */

public interface NotificationService {

    void SendNotificationToUser(NotificationRequest notifySignRequest);
}
