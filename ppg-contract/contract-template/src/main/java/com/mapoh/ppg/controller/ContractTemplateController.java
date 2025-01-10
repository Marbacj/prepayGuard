package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.TemplateRequest;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.service.TemplateService;
import com.mapoh.ppg.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mabohv
 * @date 2025/1/5 18:55
 */

@RestController
//@RequestMapping("template")
public class ContractTemplateController {

    public TemplateService templateService;

    public ContractTemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }
    public static Logger logger = LoggerFactory.getLogger(ContractTemplateController.class);

    @RequestMapping("/index")
    public String login() {
        return "index";
    }

    @RequestMapping(value = "/build", method = RequestMethod.POST)
    public CommonResponse<String> buildTemplate(@RequestBody TemplateRequest request) {
        if(request == null) {
            logger.error("request is null");
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(templateService.buildTemplate(request));
    }

    @GetMapping("/getTemplate")
    public CommonResponse<ContractTemplate> getTemplateByName(Integer templateId) {
        if(templateId == null) {
            logger.error("templateId is null");
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(templateService.getTemplate(templateId));
    }

//    @GetMapping("/getTemplateName")
//    public CommonResponse<String> getTemplateNameById(Integer id) {
//        if(id == null) {
//            return CommonResponse.successResponse(null);
//        }
//        return CommonResponse.successResponse(templateService.getTemplateName(id));
//    }
}
