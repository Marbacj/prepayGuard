package com.mapoh.ppg.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author mabohv
 * @date 2025/2/10 20:46
 * xxl-job for ensure contract transfer
 */

@Component
public class ContractScheduledJobHandler {

    public static final Logger logger = LoggerFactory.getLogger(ContractScheduledJobHandler.class);

    @XxlJob("ContractScheduledJob")
    public void myTransferJob(){

    }
}
