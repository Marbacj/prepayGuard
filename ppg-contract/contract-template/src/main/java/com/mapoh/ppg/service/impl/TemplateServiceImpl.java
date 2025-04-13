package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.constants.ActivationMethod;
import com.mapoh.ppg.constants.ValidityUnit;
import com.mapoh.ppg.dao.ContractTemplateDao;
import com.mapoh.ppg.dto.TemplateRequest;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.service.RedisService;
import com.mapoh.ppg.service.TemplateService;
import com.mapoh.ppg.vo.ContractTemplateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * @author mabohv
 * @date 2025/1/7 22:47
 */

@Service
public class TemplateServiceImpl implements TemplateService {

    ContractTemplateDao contractTemplateDao;

    //redis
    public RedisService redisService;

    public static Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

    @Autowired
    public TemplateServiceImpl(ContractTemplateDao contractTemplateDao,
                               RedisService redisService) {
        this.contractTemplateDao = contractTemplateDao;
        this.redisService = redisService;

    }

    @Override
    public String buildTemplate(TemplateRequest request) {
        // 验证请求对象是否为 null
        if (request == null) {
            return "The request is null";
        }

        // 从请求中获取字段
        String templateName = request.getTemplateName();
        String description = request.getDescription();
        BigDecimal unitAmount = request.getUnitAmount();
        Integer validityPeriod = request.getValidityPeriod();
        ValidityUnit validityUnit = request.getValidityUnit();
        ActivationMethod activationMethod = request.getActivationMethod();
        Boolean refundable = request.getRefundable();
        String refundPolicy = request.getRefundPolicy();
        String termsAndConditions = request.getTermsAndConditions();

        // 验证模板名称是否重复
        boolean exists = contractTemplateDao.existsByTemplateName(templateName);
        if (exists) {
            return "failed";
        }

        // 创建 ContractTemplate 实体对象
        ContractTemplate contractTemplate = new ContractTemplate();
        contractTemplate.setTemplateName(templateName);
        contractTemplate.setDescription(description);
        contractTemplate.setUnitAmount(unitAmount);
        contractTemplate.setValidityPeriod(validityPeriod);
        contractTemplate.setValidityUnit(validityUnit);
        contractTemplate.setActivationMethod(activationMethod);
        contractTemplate.setRefundable(refundable);
        contractTemplate.setRefundPolicy(refundPolicy);
        contractTemplate.setTermsAndConditions(termsAndConditions);
        contractTemplate.setCreatedAt(LocalDateTime.now());
        contractTemplate.setUpdatedAt(LocalDateTime.now());

        try {
            // 保存到数据库
            contractTemplateDao.save(contractTemplate);
            logger.info("Create new template {}", contractTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        redisService.addTemplateToCache(templateName, contractTemplate);

        logger.info("tag={}", contractTemplateDao.existsByTemplateName(templateName));
        return "success";
    }

    @Override
    public ContractTemplateResponse getTemplate(Integer templateId) {

        String templateName = contractTemplateDao.findTemplateNameByTemplateId(templateId);
        if(templateName == null) {
            logger.info("error template request and templateName is null {}", templateId);
            return null;
        }
        ContractTemplate template = redisService.getTemplateFromCache(templateName);
        Optional<ContractTemplate> contractTemplate;
        if(template != null) {
            contractTemplate = Optional.of(template);
        } else {
            contractTemplate = contractTemplateDao.getContractTemplateByTemplateId(templateId);
            redisService.addTemplateToCache(templateName, contractTemplate.get());
        }

        ContractTemplateResponse contractTemplateResponse = new ContractTemplateResponse();
        contractTemplateResponse.setUnitAmount(contractTemplate.isPresent() ? contractTemplate.get().getUnitAmount() : BigDecimal.ZERO);
        contractTemplateResponse.setValidityPeriod(contractTemplate.map(ContractTemplate::getValidityPeriod).orElse(null));
        contractTemplateResponse.setValidityUnit(contractTemplate.map(ContractTemplate::getValidityUnit).orElse(null));
        contractTemplateResponse.setActivationMethod(contractTemplate.map(ContractTemplate::getActivationMethod).orElse(null));
        contractTemplateResponse.setRefundable(contractTemplate.map(ContractTemplate::getRefundable).orElse(null));
        return contractTemplateResponse;
    }

//    @Override
//    public String getTemplateName(Integer templateId) {
//        if(templateId == null) {
//            return null;
//        }
//        return contractTemplateDao.findTemplateNameByTemplateId(templateId);
//    }


}
