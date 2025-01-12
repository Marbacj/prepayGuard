package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.constants.ActivationMethod;
import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.constants.ValidityUnit;
import com.mapoh.ppg.dao.ContractDao;
import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.entity.Contract;
import com.mapoh.ppg.feign.ContractTemplateFeign;
import com.mapoh.ppg.service.DistributionService;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractTemplateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

/**
 * @author mabohv
 * @date 2025/1/10 19:46
 */

@Service
public class DistributionServiceImpl implements DistributionService {

    ContractTemplateFeign contractTemplateFeign;

    ContractDao contractDao;

    public static Logger logger = LoggerFactory.getLogger(DistributionServiceImpl.class);

    @Autowired
    public DistributionServiceImpl(ContractTemplateFeign contractTemplateFeign,
                                   ContractDao contractDao) {
        this.contractTemplateFeign = contractTemplateFeign;
        this.contractDao = contractDao;
    }

    //todo: require location info
    //todo: try to acquire template id from template service
    //todo: change uuid to snowflake
    @Override
    public String createContract(CreateContractRequest createContractRequest) {
        String userId = createContractRequest.getUserId();
        String merchantId = createContractRequest.getMerchantId();
        Integer templateId = Integer.valueOf(createContractRequest.getTemplateId());

        // 参数校验
        if (userId == null || merchantId == null) {
            throw new IllegalArgumentException("用户ID、商户ID或模板ID不能为空");
        }

        Contract contract = new Contract();

        // 调用 Feign 获取模板
        CommonResponse<ContractTemplateResponse> response;
        try {
            response = contractTemplateFeign.getTemplateById(templateId);
        } catch (Exception e) {
            logger.error("调用模板服务失败，模板ID: {}，错误信息: {}", templateId, e.getMessage());
            throw new RuntimeException("调用模板服务失败，请稍后重试");
        }
        logger.info("response data:{}", response);
        if (response == null || response.getCode() != 200 || response.getData() == null) {
            logger.warn("未找到模板，模板ID: {}", templateId);
            throw new IllegalArgumentException("未找到模板，模板ID: " + templateId);
        }

        ContractTemplateResponse template = response.getData();
        // 将模板内容填充到合同对象
        fulfillTemplateToContract(contract, template);

        Random rand = new Random();
        long contractCode = rand.nextLong();


        // 设置合同基本信息
        contract.setUserId(Long.valueOf(userId));
        contract.setMerchantId(Long.valueOf(merchantId));
        contract.setTemplateId(templateId);
        contract.setStatus(ContractStatus.READY);
        contract.setTotalUnits(createContractRequest.getTotalUnits());
        contract.setActivationDate(LocalDate.now());
        contract.setContractCode(contractCode);
        // 保存合同
        contractDao.save(contract);

        return "合同创建成功，合同ID：" + contract.getContractId();
    }

    private void fulfillTemplateToContract(Contract contract, ContractTemplateResponse contractTemplate) {
        contract.setUnitAmount(contractTemplate.getUnitAmount());
        contract.setValidityPeriod(contractTemplate.getValidityPeriod());
        logger.info("validityUnit:{}", contractTemplate.getValidityUnit());
        contract.setValidityUnit(contractTemplate.getValidityUnit());
        logger.info("activationMethod:{}", contractTemplate.getActivationMethod());
        contract.setActivationMethod(contractTemplate.getActivationMethod());
        contract.setRefundable(contractTemplate.getRefundable());
    }
}
