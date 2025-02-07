package com.mapoh.ppg.listener;

import com.mapoh.ppg.config.RedisDelayedQueueListener;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author mabohv
 * @date 2025/2/5 23:02
 * 合同履行监听去监听redis延时队列
 * 当获取到了阻塞队列队头元素则进行处理
 *
 */

public class ContractScheduledListener implements RedisDelayedQueueListener<ContractScheduledRequest> {

    public static Logger logger = LoggerFactory.getLogger(ContractScheduledListener.class);
    private static final String Topic = "contract-scheduled";

    private final KafkaTemplate<String, ContractScheduledRequest> kafkaTemplate;

    ContractScheduledListener(KafkaTemplate<String, ContractScheduledRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    /**
     * 这里是
     * @param contractScheduledRequest
     */
    @Override
    public void invoke(ContractScheduledRequest contractScheduledRequest) {
        logger.info("fulfill contract, contractId:{}, merchantId:{}", contractScheduledRequest.getContractId(), contractScheduledRequest.getMerchantId());
        kafkaTemplate.send(Topic, contractScheduledRequest);
    }
}
