package com.mapoh.ppg.handler;

import com.mapoh.ppg.dao.TransactionDao;
import com.mapoh.ppg.entity.Transaction;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author mabohv
 * @date 2025/2/10 20:46
 * xxl-job for ensure contract transfer
 */

@Component
public class ContractScheduledJobHandler {

    public static final Logger logger = LoggerFactory.getLogger(ContractScheduledJobHandler.class);

    @Resource
    private TransactionDao transactionDao;


    @XxlJob("ContractScheduledJob")
    public void myTransferJob() throws InterruptedException {
//        XxlJobHelper.log("Xxl Job, Hello world");
//        for (int i = 0; i < 5; i++) {
//            XxlJobHelper.log("beat at:" + i);
//            TimeUnit.SECONDS.sleep(2);
//        }
        XxlJobHelper.log("contract scheduled job");
        String status = Transaction.TransactionStatus.FAILED.name();
        List<Long> TransactionIds = transactionDao.getTransactionIdsByStatus(status);
    }
}
