package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.service.AdminService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mabohv
 * @date 2025/2/16 21:44
 */

@RestController
public class AdminServiceImpl implements AdminService {

    @Override
    public Boolean auditRefund() {
        return null;
    }
}
