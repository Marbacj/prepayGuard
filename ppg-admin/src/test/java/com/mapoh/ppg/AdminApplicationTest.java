package com.mapoh.ppg;

import com.mapoh.ppg.dao.RefundDao;
import com.mapoh.ppg.entity.Refund;
import com.mapoh.ppg.service.AdminService;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author mabohv
 * @date 2025/3/2 14:51
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class AdminApplicationTest {

    @Autowired
    AdminService adminService;

    @Autowired
    RefundDao refundDao;
    @Test
    public void testQueryList(){

        System.out.println(adminService.getRefundList(10,10,"PENDING"));
    }

    @Test
    public void testQueryRefund(){
        adminService.getRefundRecord(10L);
    }
}
