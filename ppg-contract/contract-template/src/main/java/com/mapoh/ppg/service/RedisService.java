package com.mapoh.ppg.service;

import com.mapoh.ppg.entity.ContractTemplate;

/**
 * @author mabohv
 * @date 2025/1/15 09:38
 */

public interface RedisService {
    void addTemplateToCache(String templateName, ContractTemplate contractTemplate);

    public void deleteTemplateFromCache(String templateName);

    public ContractTemplate getTemplateFromCache(String templateName);
}
