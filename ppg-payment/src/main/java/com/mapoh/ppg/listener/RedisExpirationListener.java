package com.mapoh.ppg.listener;

import com.mapoh.ppg.dao.TransactionDao;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import com.mapoh.ppg.entity.Transaction;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Set;

/**
 * @author mabohv
 * @date 2025/3/2 15:47
 */

//消息过期监听器
@Configuration
public class RedisExpirationListener {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    public final static Logger logger = LoggerFactory.getLogger(RedisExpirationListener.class);

    @Autowired
    private TransactionDao transactionDao;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());

        // 订阅所有键过期事件（需 Redis 配置 notify-keyspace-events=Ex）
        container.addMessageListener((message, pattern) -> {
            String expiredKey = new String(message.getBody());
            if (expiredKey.startsWith("contract:cancel:")) {
                String contractId = expiredKey.replace("contract:cancel:", "");
                handleCancelEvent(contractId);
            }
        }, new PatternTopic("__keyevent@*__:expired"));

        return container;
    }

    // 处理过期事件：清理后续任务
    private void handleCancelEvent(String contractId) {
        String cancelKey = "contract:cancel:" + contractId;
        String currentInstallmentStr = redisTemplate.opsForValue().get(cancelKey);

        if (currentInstallmentStr != null) {
            int currentInstallment = Integer.parseInt(currentInstallmentStr);

            // 从 Set 中移除所有后续任务ID
            String taskKey = "contract_tasks:" + contractId;
            Set<String> taskIds = redisTemplate.opsForSet().members(taskKey);

            taskIds.stream()
                    .filter(taskId -> {
                        int installment = Integer.parseInt(taskId.split("_")[3]);
                        return installment > currentInstallment;
                    })
                    .forEach(taskId -> {
                        redisTemplate.opsForSet().remove(taskKey, taskId);
                        // 从延迟队列中移除任务
                        ContractScheduledRequest request = buildRequestFromTaskId(taskId);
                        RDelayedQueue<ContractScheduledRequest> delayedQueue = redissonClient.getDelayedQueue(
                                redissonClient.getBlockingQueue(ContractScheduledListener.class.getName())
                        );
                        delayedQueue.remove(request);
                    });

            logger.info("合同 {} 当前期 {} 已结束，已移除后续任务", contractId, currentInstallment);
        }
    }

    private ContractScheduledRequest buildRequestFromTaskId(String taskId) {
        // 根据 taskId 构造 ContractScheduledRequest 对象
        String[] parts = taskId.split("_");
        Long contractId = Long.valueOf(parts[0]);
        int installment = Integer.parseInt(parts[3]);

        ContractScheduledRequest request = new ContractScheduledRequest();
        request.setContractId(contractId);
        request.setInstallment(installment);

        Transaction transaction = transactionDao.getTransactionByContractId(contractId);

        Long userId = transaction.getUserId();
        Long merchantId = transaction.getMerchantId();

        request.setUserId(userId);
        request.setMerchantId(merchantId);
        return request;
    }
}