package com.mapoh.ppg.config;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author mabohv
 * @date 2025/2/5 21:17
 * init delay queue Listener
 */


@Component
public class RedisDelayQueueInit implements ApplicationContextAware {

    public static Logger logger = LoggerFactory.getLogger(RedisDelayQueueInit.class);

    @Resource
    RedissonClient redissonClient;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Map<String, RedisDelayedQueueListener> map = applicationContext.getBeansOfType(RedisDelayedQueueListener.class);
        for(Map.Entry<String, RedisDelayedQueueListener> entry : map.entrySet()) {
            String listenerName = entry.getKey();
            startThread(listenerName, entry.getValue());
        }
    }


    private <T> void startThread(String listenerName, RedisDelayedQueueListener taskEventListenerEntry) {
        RBlockingQueue<T> queue = redissonClient.getBlockingQueue(listenerName);
        Thread thread = new Thread(() -> {
            logger.info("启动监听队列线程" + listenerName);
            while(true) {
                try {
                    //用于从阻塞队列获取并且移除对头元素
                    T t = queue.take();
                    logger.info("获取到队列线程：{},获取到队列值{}", listenerName, t);
                    new Thread(() ->{
                        taskEventListenerEntry.invoke(t);
                    }).start();
                } catch (InterruptedException e) {
                    logger.warn("监听到线程错误",e);
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                        }
                }
            }
        });
        thread.setName(listenerName);
        thread.start();
    }
}
