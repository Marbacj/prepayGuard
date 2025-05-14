package com.mapoh.ppg;

import com.mapoh.ppg.service.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mabohv
 * @date 2025/4/7 09:54
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaymentApplicationTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void testTodayIncome(){
        Long merchantId = 11L;
        paymentService.getMerchantTodayIncome(merchantId);
        System.out.println(paymentService.getMerchantTodayIncome(merchantId));
    }
}
