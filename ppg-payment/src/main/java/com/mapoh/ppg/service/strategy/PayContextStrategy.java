package com.mapoh.ppg.service.strategy;

import com.mapoh.ppg.service.PayStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mabohv
 * @date 2025/2/24 14:31
 */

@Slf4j
@Component
public class PayContextStrategy {

    private static final String balance = "10001";
    private static final String paypal = "10002";

    //容器: map
    private static Map<String, PayStrategy> strategyMap = new ConcurrentHashMap<>();

    static {
        strategyMap.put(balance, new BalanceStrategy());
        strategyMap.put(paypal, new PaypalStrategy());
    }

    public static PayStrategy getStrategy(String payType) {
        return strategyMap.get(payType);
    }

    public boolean pay(String payCode, BigDecimal amount) {
        PayStrategy payStrategy = getStrategy(payCode);
        if(payStrategy == null) {
            log.info("there is no pay strategy for payCode={}", payCode);
            return false;
        }
        return payStrategy.pay(amount);
    }
}
