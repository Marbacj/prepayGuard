package com.mapoh.ppg;

import com.mapoh.ppg.service.DistributionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mabohv
 * @date 2025/2/7 13:30
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContractDistributionApplication.class)
public class ContractVoDistributionApplicationTest {

    @Autowired
    DistributionService distributionService;

    @Test
    public void test(){
        System.out.println(distributionService.getUnitAmount(2L));
    }



    @Test
    public void test2(){
        System.out.println(distributionService.getPendingOrderByMerchantId(1000006L));
    }

    @Test
    public void test3(){
        System.out.println(distributionService.getContractVoByContractId(1014L));
    }

    @Test
    public void test4(){
        System.out.println(distributionService.getUserHistoryContractList(1002873L));
    }
}
