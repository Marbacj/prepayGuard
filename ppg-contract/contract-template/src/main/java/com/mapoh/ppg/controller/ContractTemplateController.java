package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSONObject;
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
@CrossOrigin
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
     *     merchantId
     *     templateName: "测试模版",
     *     description: "测试模版",
     *     unitAmount: 0.01,
     *     validityPeriod: 1,
     *     validityUnit: "月",
     *     public enum ValidityUnit {
     *
     *     WEEK("周",1),
     *     MONTH("月", 2),
     *     YEAR("年", 3);
     *     activationMethod: "首次消费生效",@Getter
     * public enum ActivationMethod {
     *
     *     FIRSTUSE("首次消费", 1),
     *     SPEICALDAY("具体日期", 2),
     *     LASTDAY("最迟生效日期", 3);
     *
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
    @PostMapping("/build")
    public CommonResponse<String> buildTemplate(@RequestBody TemplateRequest request) {
        if(request == null) {
            logger.error("request is null");
            return CommonResponse.successResponse(null);
        }
        return CommonResponse.successResponse(templateService.buildTemplate(request));
    }


    /**
     * req:{
     *
     * }
     * response:{
     *
     * }
     * @param templateId
     * @return
     */
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


    /**
     * req:{
     *     merchantId:
     * }
     * response:{
     *     "templateId":
     *     "templateName"
     *     "description":
     *     "unitAmount":
     *     "validityPeriod":
     *     "validityUnit": //"WEEK","MONTH","YEAR"
     *     "activationMethod":  //"FIRSTUSE","SPEICALDAY","LASTDAY"
     *     "refundable":
     *     "refundPolicy":
     *     "termsAndConditions":
     * }
     * @param merchantId
     * @return
     */
    @GetMapping("/getTemplateInfoList/{merchantId}")
    public CommonResponse<JSONObject> getTemplateInfoList(@PathVariable("merchantId") Long merchantId) {
        return CommonResponse.successResponse(templateService.getTemplateInfoList(merchantId));
    }


}
