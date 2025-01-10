package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.TemplateRequest;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.vo.CommonResponse;

/**
 * @author mabohv
 * @date 2025/1/7 22:45
 */

public interface TemplateService {

    String buildTemplate(TemplateRequest request);

    ContractTemplate getTemplate(Integer templateId);

//    String getTemplateName(Integer templateId);
}
