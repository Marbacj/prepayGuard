package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author mabohv
 * @date 2025/1/15 09:38
 */


@Service
public class RedisServiceImpl implements RedisService {

    public static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Resource
    RedisTemplate<String,JSONObject> redisTemplate;

    private static final String TEMPLATE_PREFIX = "contract:template:";


    /**
     * 将合同模板缓存到 Redis
     */
    @Override
    public void addTemplateToCache(String templateName, ContractTemplate template) {
        if (redisTemplate == null) {
            logger.error("RedisTemplate 注入失败！");
            throw new IllegalStateException("RedisTemplate 未初始化");
        }

        if (templateName == null || template == null) {
            logger.error("模板名称或模板对象为空：templateName={}, template={}", templateName, template);
            throw new IllegalArgumentException("参数不能为空！");
        }

        String key = TEMPLATE_PREFIX + templateName;
        // 改成 Hash 存储
        redisTemplate.opsForHash().put(key, "template", JSON.toJSONString(template));
        logger.info("成功将模板存入 Redis，key={}", key);
    }

    /**
     * 从 Redis 获取合同模板
     */
    /**
     * 从 Redis 获取合同模板
     * @param templateName 模板名称
     * @return {@link ContractTemplate}
     */
    @Override
    public ContractTemplate getTemplateFromCache(String templateName) {
        String redisKey = TEMPLATE_PREFIX + templateName;

        // 从 Redis 中获取模板的 JSON 字符串
        Object templateJson = redisTemplate.opsForHash().get(redisKey, "template");

        if (templateJson == null) {
            logger.warn("No contract template found in cache for templateName: {}", templateName);
            return null;
        }

        // 反序列化 JSON 字符串为 ContractTemplate 对象
        return JSON.parseObject(templateJson.toString(), ContractTemplate.class);
    }

    /**
     * 删除 Redis 中的合同模板
     */
    @Override
    public void deleteTemplateFromCache(String templateName) {
        String key = TEMPLATE_PREFIX + templateName;
        redisTemplate.delete(key);
    }
}
