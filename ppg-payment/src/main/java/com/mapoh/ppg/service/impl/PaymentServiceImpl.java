package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.dto.ContractScheduledRequest;
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

    RedisDelayedQueue redisDelayedQueue;

    @Resource
    public RedisTemplate<String, JSONObject> redisTemplate;

    @Resource
    public RedissonClient redissonClient;

    @SuppressWarnings("all")
    PaymentServiceImpl(ContractServiceFeign contractServiceFeign,
                       UserServiceFeign userServiceFeign,
                       RedisDelayedQueue redisDelayedQueue) {
        this.contractServiceFeign = contractServiceFeign;
        this.userServiceFeign = userServiceFeign;
        this.redisDelayedQueue = redisDelayedQueue;
    }

    /**
     *
     * 使用余额进行支付
     * 先进行扣除用户余额
     * 其次进行修改合同状态
     * 将合同放入延迟队列中进行生效
     * @param balancePaymentRequest
     * @return
     */
    @Override
    public Boolean payInBalance(BalancePaymentRequest balancePaymentRequest) {
        Long userId = balancePaymentRequest.getUserId();
        Long contractId = balancePaymentRequest.getContractId();
        Status status = balancePaymentRequest.getStatus();

        if(userId == null || contractId == null || status == null) {
            logger.info("It's missed excepted param");
        }
        BigDecimal amount = contractServiceFeign.getAmount(contractId);

        BigDecimal balance = userServiceFeign.getBalance(userId);

        if(balance.compareTo(amount) < 0) {
            logger.error("the user balance is not enough:{}", balance);
            return false;
        }

        try {
            userServiceFeign.settlement(userId, amount);
            contractServiceFeign.validContract(contractId);
            ContractScheduledRequest contractScheduledRequest = new ContractScheduledRequest();
            contractScheduledRequest.setContractId(contractId);
            redisDelayedQueue.addQueue(contractScheduledRequest, 10, TimeUnit.SECONDS, ContractScheduledListener.class.getName());
            return true;
        }catch (Exception e) {
            logger.error("change status or settlement error:{}", e.getMessage());
            return false;
        }
    }
}
