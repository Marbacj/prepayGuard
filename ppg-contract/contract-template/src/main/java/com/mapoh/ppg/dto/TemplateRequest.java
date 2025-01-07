package com.mapoh.ppg.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mabohv
 * @date 2025/1/7 21:30
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {
    public String name;
    public String description;
    public String fields;
}
