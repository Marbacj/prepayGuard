package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.service.AdminService;
import com.mapoh.ppg.vo.CommonResponse;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mabohv
 * @date 2025/2/16 20:14
 */

@RestController
public class AdminController {

    @Resource
    private AdminService adminService;


    /**request:
     * {
     *     "page": "",
     *     "size: " ",
     *     "status:" "
     *
     * }
     * response:
     * {
     *     "id":{
     *         "userId":
     *         "contractId":
     *         "status":
     *         "create_time"
     *         "update_time"
     *     }
     * }
     * @param refundRequest
     * @return
     */
    @GetMapping("/list")
    public CommonResponse<List<JSONObject>> queryRefundList(@RequestBody JSONObject refundRequest) {
        int page = refundRequest.getInteger("page");
        int size = refundRequest.getInteger("size");
        String status = refundRequest.getString("status");

        return CommonResponse.successResponse(adminService.getRefundList(page, size, status));
    }

    @GetMapping("/{refundId}")
    public CommonResponse<JSONObject> getRefund(@PathVariable("refundId") Long refundId) {
        return CommonResponse.successResponse(adminService.getRefundRecord(refundId));
    }

    /**
     * {
     *     "refundId":"",
     *     "status":"",
     *     "reason":""
     * }
     * @param refundRequest
     * @return
     */
    @PutMapping("/process")
    public CommonResponse<Boolean> processRefund(@RequestBody JSONObject refundRequest) {
        // 参数校验
        Long refundId = refundRequest.getLong("refundId");
        String status = refundRequest.getString("status");
        String reason = refundRequest.getString("reason");

        Boolean result = adminService.processRefund(refundId, status, reason);
        return CommonResponse.successResponse(result);
    }
}
