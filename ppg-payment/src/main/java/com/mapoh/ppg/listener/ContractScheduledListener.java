package com.mapoh.ppg.listener;

import com.mapoh.ppg.config.RedisDelayedQueueListener;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mabohv
 * @date 2025/2/5 23:02
 * 合同履行监听去监听redis延时队列
 * 当获取到了阻塞队列队头元素则进行处理
 *
 */

@Component
public class ContractScheduledListener implements RedisDelayedQueueListener<ContractScheduledRequest> {

    public static Logger logger = LoggerFactory.getLogger(ContractScheduledListener.class);
    private static final String TOPIC = "contract-scheduled";

    @Resource
    private final KafkaTemplate<String, ContractScheduledRequest> kafkaTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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
        kafkaTemplate.send(TOPIC, contractScheduledRequest);

        String taskId = contractScheduledRequest.getContractId() + "_installment_" + contractScheduledRequest.getInstallment();
        redisTemplate.opsForSet().remove("contract_tasks" + contractScheduledRequest.getContractId(), taskId);
    }
}
