package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.TemplateRequest;
import com.mapoh.ppg.vo.CommonResponse;

/**
 * @author mabohv
 * @date 2025/1/7 22:45
 */

public interface TemplateService {

    public String buildTemplate(TemplateRequest request);

}
