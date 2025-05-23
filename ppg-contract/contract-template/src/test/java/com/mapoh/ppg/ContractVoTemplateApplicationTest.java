package com.mapoh.ppg;


import com.mapoh.ppg.service.TemplateService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/9 20:41
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractVoTemplateApplicationTest {

    @Autowired
    TemplateService templateService;

//    @Test
//    public void testBuild(){
//        TemplateRequest templateRequest = new TemplateRequest();
//        templateRequest.setTemplateName("test");
//        templateRequest.setDescription("test");
//        templateRequest.setUnitAmount(new BigDecimal(30));
//        templateRequest.setValidityPeriod(1);
//        templateRequest.setActivationMethod(String.valueOf(ActivationMethod.FIRSTUSE));
//        System.out.println(templateRequest.getActivationMethod());
//        templateRequest.setValidityUnit(String.valueOf(ValidityUnit.WEEK));
//
//        templateService.buildTemplate(templateRequest);
//    }
    @Test
    public void test(){
        System.out.println(templateService.getTemplate(1));
    }

    @Test
    public void test1(){
        System.out.println(templateService.getTemplateInfoList(1L));
    }
}
