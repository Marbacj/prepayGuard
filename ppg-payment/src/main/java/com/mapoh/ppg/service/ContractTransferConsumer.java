package com.mapoh.ppg.service;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dao.TransactionDao;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import com.mapoh.ppg.dto.payment.TransferRequest;
import com.mapoh.ppg.entity.Transaction;
import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.feign.MerchantServiceFeign;
import com.mapoh.ppg.listener.HotSpotAccountListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author mabohv
 * @date 2025/2/7 10:01
 *
 * 	1.	分区和副本数的合理配置；
 * 	2.	消费者组的设计与并发控制；
 * 	3.	消息顺序的保证；
 * 	4.	幂等性和消息去重；
 * 	5.	消息过期与死信队列机制；
 * 	6.	监控、报警和重试策略的优化。
 *
 * 	分布式锁处理转账
 *
 * 		Redisson分布式锁：用于确保高并发下多个请求对同一商家账户的修改是串行的，避免资源竞争。
 * 	•	CAS+版本号：用于实现无锁化的余额变更，保证在并发情况下不会出现数据冲突，同时减少锁的使用，提高性能。
 */

@Service
public class ContractTransferConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ContractTransferConsumer.class);

    // 最大重试次数
    private static final int MAX_RETRY_COUNT = 3;
    // 重试间隔，单位：毫秒
    private static final long RETRY_INTERVAL = 5000;

    public ContractServiceFeign contractServiceFeign;
    public MerchantServiceFeign merchantServiceFeign;
    public PaymentService paymentService;
    public HotSpotAccountListener hotSpotAccountListener;

    @Resource
    public TransactionDao transactionDao;

    @Resource
    public RedissonClient redissonClient;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @SuppressWarnings("all")
    public ContractTransferConsumer(ContractServiceFeign contractServiceFeign,
                                    MerchantServiceFeign merchantServiceFeign,
                                    PaymentService paymentService,
                                    HotSpotAccountListener hotSpotAccountListener) {
        this.contractServiceFeign = contractServiceFeign;
        this.merchantServiceFeign = merchantServiceFeign;
        this.paymentService = paymentService;
        this.hotSpotAccountListener = hotSpotAccountListener;
    }

    @KafkaListener(topics = "contract-scheduled", groupId = "contract-group")
    public void handleContractTransfer(ConsumerRecord<String, ContractScheduledRequest> record) {
        ContractScheduledRequest contractScheduledRequest = record.value();
        Long contractId = contractScheduledRequest.getContractId();
        Long merchantId = contractScheduledRequest.getMerchantId();
        Long userId = contractScheduledRequest.getUserId();



        logger.info("Processing contract transfer record: merchantId = {}, contractId = {} ", merchantId, contractId);

        BigDecimal unitAmount = contractServiceFeign.getUnitAmount(contractId).getData();
        if (unitAmount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("No valid amount retrieved for contractId: {}", contractId);
            return;
        }
        logger.info("Processing transfer for contract:{} - user:{} merchant:{} unitAmount:{}",
                contractId, userId, merchantId, unitAmount);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setMerchantId(merchantId);
        transaction.setContractId(contractId);
        transaction.setAmount(unitAmount);


        processTransferAsync(contractId, merchantId, unitAmount, transaction);

        logger.debug("Completed processing for contract:{}", contractId);
    }

    //使用线程池拉高消费速率
    // 使用看门狗自动续期（默认30秒，无需显式设置时间）
    @Async
    public void processTransferAsync(Long contractId, Long merchantId, BigDecimal unitAmount, Transaction transaction) {
        if(hotSpotAccountListener.isHotSpot(merchantId)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contractId", merchantId);
            jsonObject.put("unitAmount", unitAmount);
            merchantServiceFeign.updateBalanceWithCAS(jsonObject);
        } else {
            try {
                RLock merchantLock = redissonClient.getLock("merchantLock" + merchantId);
                try{
                    merchantLock.lock();
                    // 异步处理
                    merchantServiceFeign.receiveTransferAccount(new TransferRequest(merchantId, unitAmount));
                }finally {
                    merchantLock.unlock();
                }
                logger.debug("Initiating transfer to merchant:{}, amount:{}", merchantId, unitAmount);


                transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
                transactionDao.insertOrUpdateTransactionSuccess(transaction);
                logger.info("Transfer SUCCESS - contract:{} merchant:{} amount:{}", contractId, merchantId, unitAmount);
            } catch (Exception e) {
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
                transactionDao.insertOrUpdateTransactionFail(transaction);
                String errorMsg = String.format("Transfer FAILED - contract:%s merchant:%s amount:%s", contractId, merchantId, unitAmount);
                logger.error(errorMsg, e);

                // 判断是否需要进入重试队列或死信队列
                if (transaction.getRetryCount() < MAX_RETRY_COUNT) {
                    logger.info("Retrying transaction for contract:{} merchant:{} attempt:{}", contractId, merchantId, transaction.getRetryCount() + 1);
                    // 增加重试次数
                    transaction.setRetryCount(transaction.getRetryCount() + 1);
                    transactionDao.updateRetryCount(transaction);

                    // 将消息推送到重试队列
                    kafkaTemplate.send("contract-scheduled-retries", transaction);
                } else {
                    logger.error("Max retry attempts reached for contract:{} merchant:{}, sending to dead letter queue", contractId, merchantId);
                    // 将消息推送到死信队列
                    kafkaTemplate.send("contract-scheduled-dlq", transaction);
                }

                throw new RuntimeException(errorMsg, e);
            }
        }
    }

    // 重试队列消费
    // 加入幂等性校验
    //todo: certain hot account
    @KafkaListener(topics = "contract-scheduled-retries", groupId = "contract-group-retries")
    public void handleRetryTransaction(ConsumerRecord<String, Transaction> record) {
        Transaction transaction = record.value();
        Long contractId = transaction.getContractId();
        Long merchantId = transaction.getMerchantId();
        BigDecimal unitAmount = transaction.getAmount();



        if(paymentService.isProccessed(transaction.getTransactionId())){
            return;
        }

        logger.info("Retrying transaction for contract: {}", contractId);


        try {
            RLock merchantLock = redissonClient.getLock("merchantLock" + merchantId);
            try{
                merchantLock.lock();
                if (transaction.getRetryCount() < MAX_RETRY_COUNT) {
                    processTransferAsync(contractId, merchantId, unitAmount, transaction);
                }
            } finally {
                merchantLock.unlock();
            }
            // 尝试再次进行转账
            merchantServiceFeign.receiveTransferAccount(new TransferRequest(merchantId, unitAmount));

            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            transactionDao.insertOrUpdateTransactionSuccess(transaction);
            logger.info("Retry SUCCESS for contract:{} merchant:{} amount:{}", contractId, merchantId, unitAmount);
        } catch (Exception e) {
            //退避策略增强
            logger.error("Retry failed for contract:{} merchant:{}, scheduling another retry", contractId, merchantId, e);
            // 将消息再次推送到重试队列，增加延迟（可以增加延迟时间）
            // 在重试发送时添加延时
            kafkaTemplate.send(
                    "contract-scheduled-retries",
                    null,
                    System.currentTimeMillis(),
                    null,
                    transaction
            ).addCallback(
                    result -> {},
                    ex -> logger.error("Retry send failed", ex)
            );
        }
    }


    // 死信队列消费
    @KafkaListener(topics = "contract-scheduled-dlq", groupId = "contract-group-dlq")
    public void handleDeadLetterTransaction(ConsumerRecord<String, Transaction> record) {
        Transaction transaction = record.value();
        logger.error("Dead letter transaction for contract:{} merchant:{}", transaction.getContractId(), transaction.getMerchantId());
        // 此处可以将失败的交易记录到数据库或发送通知给管理员等操作
    }

    @KafkaListener(topics = "contract-payment", groupId = "payment-group")
    public void consumePaymentMessage(ConsumerRecord<String, String> record) {
        try {
            String message = record.value();
            logger.info("Received payment message: {}", message);

            // 解析 JSON 数据（可以用 Jackson 解析）
            // JSONObject jsonObject = new JSONObject(message);
            // Long userId = jsonObject.getLong("userId");
            // Long contractId = jsonObject.getLong("contractId");
            // BigDecimal amount = new BigDecimal(jsonObject.getString("amount"));

            // 处理消息（如更新数据库、通知商户）
            logger.info("Processing payment for message: {}", message);
        } catch (Exception e) {
            logger.error("Error processing Kafka message: {}", e.getMessage());
        }
    }
}