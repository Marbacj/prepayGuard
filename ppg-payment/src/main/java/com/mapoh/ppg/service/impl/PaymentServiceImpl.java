package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dao.TransactionDao;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import com.mapoh.ppg.dto.RefundRequest;
import com.mapoh.ppg.dto.payment.SettlementRequest;
import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.feign.UserServiceFeign;
import com.mapoh.ppg.listener.ContractScheduledListener;
import com.mapoh.ppg.service.PaymentService;
import com.mapoh.ppg.utils.RedisDelayedQueue;
import com.mapoh.ppg.vo.ContractVo;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
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
    public KafkaTemplate<String, RefundRequest> kafkaTemplate;

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
        BigDecimal amount = contract.getTotalAmount();

        BigDecimal balance = userServiceFeign.getBalance(userId).getData();

        if(balance.compareTo(amount) < 0) {
            logger.error("the user balance is not enough:{}", balance);
            return false;
        }
        try {

            userServiceFeign.settlement(new SettlementRequest(userId, amount));
            Boolean changedResult = contractServiceFeign.validContract(contractId).getData();
            logger.info("contract total necessary amount = {}", amount);
            logger.info("contract change status:{}", changedResult);
            logger.info("now the contract is valid");

            Long merchantId = contract.getMerchantId();

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
        }catch (Exception e) {
            logger.error("change status or settlement error:{}", e.getMessage());
            return false;
        }
    }


    /**
     * 审核过程需要较长时间，可以通过消息通知来告知客户退款是否通过。
     * 后台服务在审核完退款申请后可以通过推送、邮件或短信通知用户或前端。
     * @param refundRequest
     * @return
     */
    @Override
    public Boolean refundBalance(RefundRequest refundRequest) {

        Long userId = refundRequest.getUserId();
        Long contractId = refundRequest.getContractId();
        String refundReason = refundRequest.getReason();

        kafkaTemplate.send(TOPIC, refundRequest);
        return true;
    }

    @Override
    public Boolean isProccessed(Long transactionId){
        return transactionDao.checkStatusByTransactionId(transactionId);
    }

    @Override
    public BigDecimal getTotalFee(Long contractId) {
        return contractServiceFeign.getAmount(contractId).getData();
    }
}
