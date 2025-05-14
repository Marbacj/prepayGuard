package com.mapoh.ppg.service;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dto.TemplateRequest;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractTemplateResponse;

/**
 * @author mabohv
 * @date 2025/1/7 22:45
 */

public interface TemplateService {

    String buildTemplate(TemplateRequest request);

    ContractTemplateResponse getTemplate(Integer templateId);

    JSONObject getTemplateInfoList(Long merchantId);

//    String getTemplateName(Integer templateId);
}
