package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dao.TransactionDao;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import com.mapoh.ppg.dto.RefundRequest;
import com.mapoh.ppg.dto.payment.SettlementRequest;
import com.mapoh.ppg.entity.Transaction;
import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.feign.UserServiceFeign;
import com.mapoh.ppg.listener.ContractScheduledListener;
import com.mapoh.ppg.service.PaymentService;
import com.mapoh.ppg.utils.RedisDelayedQueue;
import com.mapoh.ppg.vo.ContractVo;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.mapoh.ppg.entity.Transaction.TransactionStatus.FAILED;
import static com.mapoh.ppg.entity.Transaction.TransactionStatus.SUCCESS;

/**
 * @author mabohv
 * @date 2025/1/14 21:20
 */

@Service
public class PaymentServiceImpl implements PaymentService {

    public static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    ContractServiceFeign contractServiceFeign;

    UserServiceFeign userServiceFeign;

    @Resource
    RedisDelayedQueue redisDelayedQueue;

    @Resource
    public RedisTemplate<String, String> redisTemplate;

    @Resource
    RedissonClient redissonClient;

    @Autowired
    private TransactionDao transactionDao;

    @SuppressWarnings("all")
    PaymentServiceImpl(ContractServiceFeign contractServiceFeign,
                       UserServiceFeign userServiceFeign) {
        this.contractServiceFeign = contractServiceFeign;
        this.userServiceFeign = userServiceFeign;
    }
    private final static String TOPIC = "contract-refund";

    @Resource
    public KafkaTemplate<String,Object> kafkaTemplate;

    private final static String TRANSACTION_TOPIC = "contract-payment";

    /**
     *
     * 使用余额进行支付
     * 先进行扣除用户余额
     * 其次进行修改合同状态
     * 将合同放入延迟队列中进行生效
     *
     * problem: add the
     * @param balancePaymentRequest
     * @return
     * todo: add the function that transfer scheduled and add query the merchant from contracts
     */
    @Override
    public Boolean payInBalance(BalancePaymentRequest balancePaymentRequest) {

        Long userId = balancePaymentRequest.getUserId();
        Long contractId = balancePaymentRequest.getContractId();

        if(userId == null || contractId == null) {
            logger.info("It's missed excepted param");
        }
        ContractVo contract = contractServiceFeign.getContractVo(contractId).getData();
        logger.info("this is contract info:{}", contract);

        BigDecimal amount = contract.getTotalAmount();

        BigDecimal balance = userServiceFeign.getBalance(userId).getData();
        Long merchantId = contract.getMerchantId();
        if(balance.compareTo(amount) < 0) {
            logger.error("the user balance is not enough:{}", balance);
            return false;
        }

        Transaction transaction = new Transaction(contractId, userId, amount);
        transaction.setMerchantId(merchantId);
            try {
                Boolean settlementResult = userServiceFeign.settlement(new SettlementRequest(userId, amount)).getData();
                if(!settlementResult) {
                    throw new RuntimeException("settlement failed");
                }
                Boolean changedResult = contractServiceFeign.validContract(contractId).getData();
                if(!changedResult) {
                    throw new RuntimeException("valid_contract failed");
                }
                transaction.setStatus(SUCCESS);
                logger.info("Transaction commited successfully");

            } catch (Exception e) {
                transaction.setStatus(FAILED);
                logger.error("Transaction failed");
                throw new RuntimeException(e);
            }
            logger.info("insert transaction:{}", transaction);
            transactionDao.save(transaction);



            ContractScheduledRequest contractScheduledRequest = new ContractScheduledRequest();
            contractScheduledRequest.setContractId(contractId);
            contractScheduledRequest.setMerchantId(merchantId);
            contractScheduledRequest.setUserId(userId);

            Timestamp timestamp = contract.getUnitTime();
            Integer validityUnit = contract.getValidityPeriod();
//            RedisDelayedQueue redisDelayedQueue = new RedisDelayedQueue();
            for(int i = 1; i <= validityUnit; i++){
                long delay = i * timestamp.getTime() - System.currentTimeMillis();
                contractScheduledRequest.setInstallment(i);
                redisDelayedQueue.addQueue(contractScheduledRequest, delay, TimeUnit.MILLISECONDS, ContractScheduledListener.class.getName());

                String taskId = contractId + "_installment_" + i;

                redisTemplate.opsForSet().add("contract_tasks" + contractId, taskId);
            }
            return true;
    }


    /**
     * 审核过程需要较长时间，可以通过消息通知来告知客户退款是否通过。
     * 后台服务在审核完退款申请后可以通过推送、邮件或短信通知用户或前端。
     *
     *
     * @param refundRequest
     * @return
     */
    @Override
    public Boolean refundBalance(RefundRequest refundRequest) {

        Long userId = refundRequest.getUserId();
        Long contractId = refundRequest.getContractId();
        String refundReason = refundRequest.getReason();

        Boolean changeContractToRefund = contractServiceFeign.updateRefundStatus(contractId).getData();

        return true;
    }

    @Override
    public Boolean isProccessed(Long transactionId){
        return transactionDao.checkStatusByTransactionId(transactionId);
    }

    @Override
    public Double getMerchantTodayIncome(Long merchantId) {
        Double result = transactionDao.getIncomeByMerchantId(merchantId);
        logger.info("result : {}" , result);
        if(result == null) {
            logger.info("the merchant income is exception");
            return 0.0;
        }
        return result;
    }


    public Boolean cancelinstallment(Long contractId, int installment) {
        String taskId = contractId + "_installment_" + installment;
        String key = "contract_tasks" + contractId;

        Long removed = redisTemplate.opsForSet().remove(key, taskId);
        if(removed == null || removed == 0) {
            logger.warn("Tasks {} not found in Redis Set", taskId);
            return false;
        }
        ContractScheduledRequest contractScheduledRequest = new ContractScheduledRequest();
        contractScheduledRequest.setContractId(contractId);
        contractScheduledRequest.setInstallment(installment);

        Transaction transaction = transactionDao.getTransactionByContractId(contractId);
        Long userId = transaction.getUserId();
        Long merchantId = transaction.getMerchantId();

        contractScheduledRequest.setUserId(userId);
        contractScheduledRequest.setMerchantId(merchantId);

        RBlockingDeque<ContractScheduledRequest> blockingDeque = redissonClient.getBlockingDeque(ContractScheduledRequest.class.getName());
        RDelayedQueue<ContractScheduledRequest> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        boolean isRemoved = delayedQueue.remove(contractScheduledRequest);

        logger.info("Task remove successful: {}", taskId);
        return isRemoved;

    }

    public void cancelContract(Long contractId){

    }

    /**
     *
     * 首先删除缓存中的合同信息，
     * 同时更新数据库中的状态
     * 为了弥补redis过期事件有可能内存满了丢失事件的不足，
     * 需要加入xxl-job去删除合同，通过扫描退款表去更新合同状态
     * @param contractId
     * @param currentInstallment
     * @param remainingTimeMillis
     */
    public void cancelContractWithCurrentInstallment(Long contractId, int currentInstallment, long remainingTimeMillis) {
        String cancelKey = "contract:cancel:" + contractId;

        // 存储当前期数到 Redis，并设置剩余时间后过期
        redisTemplate.opsForValue().set(cancelKey, String.valueOf(currentInstallment));
        redisTemplate.expire(cancelKey, remainingTimeMillis, TimeUnit.MILLISECONDS);

        logger.info("合同 {} 已标记取消，当前期 {} 结束后将移除后续任务", contractId, currentInstallment);
    }

    @Override
    public BigDecimal getTotalFee(Long contractId) {
        return contractServiceFeign.getAmount(contractId).getData();
    }
}
