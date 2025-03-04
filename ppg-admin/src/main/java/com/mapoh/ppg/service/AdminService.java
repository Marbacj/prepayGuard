package com.mapoh.ppg.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author mabohv
 * @date 2025/2/16 20:17
 */

public interface AdminService {
    List<JSONObject> getRefundList(int page, int size, String status);

    JSONObject getRefundRecord(Long refundId);

    Boolean processRefund(Long refundId, String status, String reason);
}
