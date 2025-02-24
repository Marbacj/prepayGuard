package com.mapoh.ppg.controller;

import com.mapoh.ppg.service.AdminService;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author mabohv
 * @date 2025/2/16 20:14
 */

@RestController
public class AdminController {

    @Resource
    private AdminService adminService;


}
