package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dao.MerchantDao;
import com.mapoh.ppg.entity.Merchant;
import com.mapoh.ppg.feign.PaymentServiceFeign;
import com.mapoh.ppg.service.MerchantService;
import com.mapoh.ppg.vo.CommonResponse;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javassist.NotFoundException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author mabohv
 * @date 2025/2/7 21:15
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Resource
    MerchantDao merchantDao;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    public final PaymentServiceFeign paymentServiceFeign;

    @Resource
    public RedissonClient redissonClient;

    @SuppressWarnings("all")
    MerchantServiceImpl(MerchantDao merchantDao,
                        PaymentServiceFeign paymentServiceFeign) {
        this.merchantDao = merchantDao;
        this.paymentServiceFeign = paymentServiceFeign;
    }

    @Override
    @Transactional
    public Boolean addAmountByTransfer(Long merchantId, BigDecimal transferAmount) {
        try {
            merchantDao.updateMerchantAmountById(merchantId,transferAmount);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     *缓存与数据库不一致
     * 高并发下的缓存雪崩 ： 随机过期时间 + 互斥锁重建
     * 版本号回滚漏洞
     * @param merchantId
     * @param transferAmount
     * @return
     * @throws InterruptedException
     */
    @Transactional
    @Override
    public Boolean transferWithRetry(Long merchantId, BigDecimal transferAmount) throws InterruptedException, NotFoundException {
        int maxRetries = 3;
        String redisKey = "merchant:version" + merchantId;
        Integer cachedVersion = redisTemplate.opsForValue().get(redisKey);

        if(cachedVersion == null) {
            RLock lock = redissonClient.getLock("versionLock" + merchantId);
            try {
                lock.lock();
                //双重锁检查
                cachedVersion = redisTemplate.opsForValue().get(redisKey);
                if(cachedVersion == null) {
                    Merchant merchant = merchantDao.findMerchantById(merchantId);
                    cachedVersion = merchant.getVersion();
                    redisTemplate.opsForValue().set(
                            redisKey,
                            cachedVersion,
                            30 + ThreadLocalRandom.current().nextInt(10),
                            TimeUnit.SECONDS
                    );
                }
            }finally {
                lock.unlock();
            }

            redisTemplate.opsForValue().set(redisKey, cachedVersion, 30, TimeUnit.SECONDS);
        }
        // 线程A成功更新数据库版本为2，但缓存更新失败
        // 线程B读取缓存中的旧版本1发起CAS操作，导致无效重试

        //双写事务保证
        /**
         * // 在数据库事务提交后同步更新缓存
         * TransactionSynchronizationManager.registerSynchronization(
         *     new TransactionSynchronization() {
         *         @Override
         *         public void afterCommit() {
         *             redisTemplate.opsForValue().set(
         *                 redisKey,
         *                 newVersion,
         *                 30, TimeUnit.SECONDS
         *             );
         *         }
         *     }
         * );
         */
        //补偿任务兜底
        for (int i = 0; i < maxRetries; i++) {
            int rows = merchantDao.casUpdateBalance(merchantId, transferAmount, cachedVersion);
            if (rows > 0) {
                redisTemplate.opsForValue().set(redisKey, cachedVersion + 1);
                return Boolean.TRUE;
            }

            //退避策略，防止空转
            Thread.sleep((long) Math.pow(2, i) * 10);
        }
        return Boolean.FALSE;
    }

    @Override
    public Merchant getMerchantIdByMerchantName(String merchantName) {
        return merchantDao.getMerchantByMerchantName(merchantName);
    }

    @Override
    public Double getIncomeByMerchantId(Long merchantId) {
        Double income = 0.0;
        income =  paymentServiceFeign.getMerchantTodayIncome(merchantId).getData();
        return income;
    }

    @Override
    public List<JSONObject> getMerchantList(JSONObject merchantIds) {
        return Collections.emptyList();
    }

}
