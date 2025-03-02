package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.service.AdminService;
import com.mapoh.ppg.vo.CommonResponse;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


    /**
     * {
     *     "page": "",
     *     "size: " ",
     *     "status:" "
     *
     * }
     * @param refundRequest
     * @return
     */
    @GetMapping("/list")
    public CommonResponse<List<JSONObject>> list(@RequestBody JSONObject refundRequest) {
        int page = refundRequest.getInteger("page");
        int size = refundRequest.getInteger("size");
        Boolean status = refundRequest.getBoolean("status");

        return CommonResponse.successResponse()
    }
}
