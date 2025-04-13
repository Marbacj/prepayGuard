package com.mapoh.ppg.controller;

import com.mapoh.ppg.dto.TemplateRequest;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.service.TemplateService;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractTemplateResponse;
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


    /**
     * req：
     * {
     *     templateName: "测试模版",
     *     description: "测试模版",
     *     unitAmount: 0.01,
     *     validityPeriod: 1,
     *     validityUnit: "月",
     *     activationMethod: "首次消费生效",
     *     refundable: true,
     *     refundPolicy: "测试模版",
     *     termsAndConditions: "测试模版"
     * }
     * response:
     * {
     *     code:
     *     message:
     *     data:{
     *         "sucess"/"false"
     *     }
     * }
     * @param request
     * @return
     */
    @RequestMapping(value = "/build", method = RequestMethod.POST)
    public CommonResponse<String> buildTemplate(@RequestBody TemplateRequest request) {
        if(request == null) {
            logger.error("request is null");
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(templateService.buildTemplate(request));
    }

    @GetMapping("/getTemplate/{templateId}")
    public CommonResponse<ContractTemplateResponse> getTemplateById(@PathVariable("templateId") Integer templateId) {
        if (templateId == null) {
            logger.error("templateId is null");
            return CommonResponse.errorResponse(400, "模版不能为空", null);
        }

        ContractTemplateResponse template = templateService.getTemplate(templateId);

        if (template == null) {
            logger.warn("Template not found for ID: {}", templateId);
            return CommonResponse.errorResponse(404, "未找到对应的模板", null);
        }

        return CommonResponse.successResponse(template);
    }

//    @GetMapping("/getTemplateName")
//    public CommonResponse<String> getTemplateNameById(Integer id) {
//        if(id == null) {
//            return CommonResponse.successResponse(null);
//        }
//        return CommonResponse.successResponse(templateService.getTemplateName(id));
//    }
}
