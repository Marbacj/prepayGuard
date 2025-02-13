package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import com.mapoh.ppg.dto.payment.SettlementRequest;
import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.feign.UserServiceFeign;
import com.mapoh.ppg.listener.ContractScheduledListener;
import com.mapoh.ppg.service.PaymentService;
import com.mapoh.ppg.utils.RedisDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    public RedisTemplate<String, JSONObject> redisTemplate;

    @Resource
    public RedissonClient redissonClient;

    @SuppressWarnings("all")
    PaymentServiceImpl(ContractServiceFeign contractServiceFeign,
                       UserServiceFeign userServiceFeign) {
        this.contractServiceFeign = contractServiceFeign;
        this.userServiceFeign = userServiceFeign;
    }

    /**
     *
     * 使用余额进行支付
     * 先进行扣除用户余额
     * 其次进行修改合同状态
     * 将合同放入延迟队列中进行生效
     * @param balancePaymentRequest
     * @return
     * todo: add the function that transfer scheduled and add query the merchant from contracts
     */
    @Override
    public Boolean payInBalance(BalancePaymentRequest balancePaymentRequest) {

        Long userId = balancePaymentRequest.getUserId();
        Long contractId = balancePaymentRequest.getContractId();
        Status status = balancePaymentRequest.getStatus();

        if(userId == null || contractId == null || status == null) {
            logger.info("It's missed excepted param");
        }
        BigDecimal amount = contractServiceFeign.getAmount(contractId).getData();

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

            Long merchantId = contractServiceFeign.getMerchantId(contractId).getData();

            ContractScheduledRequest contractScheduledRequest = new ContractScheduledRequest();
            contractScheduledRequest.setContractId(contractId);
            contractScheduledRequest.setMerchantId(1L);
            contractScheduledRequest.setUserId(userId);
//            RedisDelayedQueue redisDelayedQueue = new RedisDelayedQueue();
            redisDelayedQueue.addQueue(contractScheduledRequest, 10, TimeUnit.SECONDS, ContractScheduledListener.class.getName());
            logger.info(" first contractScheduled 执行:{}", contractScheduledRequest.getContractId());
            redisDelayedQueue.addQueue(contractScheduledRequest, 20, TimeUnit.SECONDS, ContractScheduledListener.class.getName());
            logger.info("second contractScheduled 执行:{}", contractScheduledRequest.getContractId());
            redisDelayedQueue.addQueue(contractScheduledRequest, 30, TimeUnit.SECONDS, ContractScheduledListener.class.getName());
            logger.info("third contractScheduled 执行:{}", contractScheduledRequest.getContractId());
            return true;
        }catch (Exception e) {
            logger.error("change status or settlement error:{}", e.getMessage());
            return false;
        }
    }
}
