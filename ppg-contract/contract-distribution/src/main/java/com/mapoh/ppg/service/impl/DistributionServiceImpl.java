package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.dao.ContractDao;
import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.entity.Contract;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.feign.ContractTemplateFeign;
import com.mapoh.ppg.service.DistributionService;
import com.mapoh.ppg.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        CommonResponse<ContractTemplate> response = contractTemplateFeign.getTemplateById(templateId);

        if (response == null || response.getData() == null) {
            logger.warn("the contract template {} is not exist", templateId);
            throw new IllegalArgumentException("the contract template " + templateId + " is not exist");
        }

        ContractTemplate contractTemplate = response.getData();

        // 将模板内容填充到合同对象
        fulfillTemplateToContract(contract, contractTemplate);

        // 设置合同基本信息
        contract.setUserId(Long.valueOf(userId));
        contract.setMerchantId(Long.valueOf(merchantId));
        contract.setTemplateId(Integer.valueOf(templateId));
        contract.setStatus(ContractStatus.READY);
        contract.setTotalUnits(createContractRequest.getTotalUnits());
        contract.setTotalAmount(createContractRequest.getTotalAmount());
        contract.setActivationDate(LocalDate.now());

        // 保存合同
        contractDao.save(contract);

        return "合同创建成功，合同ID：" + contract.getContractId();
    }

    private void fulfillTemplateToContract(Contract contract, ContractTemplate contractTemplate) {
        contract.setUnitAmount(contractTemplate.getUnitAmount());
        contract.setValidityPeriod(contractTemplate.getValidityPeriod());
        contract.setValidityUnit(contractTemplate.getValidityUnit());
        contract.setActivationMethod(contractTemplate.getActivationMethod());
        contract.setRefundable(contractTemplate.getRefundable());
    }
}
