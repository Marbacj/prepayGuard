package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.constants.Role;
import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.dao.ContractDao;
import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.dto.SignContractRequest;
import com.mapoh.ppg.entity.Contract;
import com.mapoh.ppg.feign.ContractTemplateFeign;
import com.mapoh.ppg.service.DistributionService;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractTemplateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Random;

/**
 * @author mabohv
 * @date 2025/1/10 19:46
 */

@Service
public class DistributionServiceImpl implements DistributionService {

    ContractTemplateFeign contractTemplateFeign;

    ContractDao contractDao;

    public static Logger logger = LoggerFactory.getLogger(DistributionServiceImpl.class);

    @SuppressWarnings("all")
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

    /**
     *
     *
     * @param signContractRequest
     * @return
     */
    @Transactional
    @Override
    public String signContract(SignContractRequest signContractRequest) {
        String role = signContractRequest.getRole().toString();
        Long contractId = signContractRequest.getContractId();
        Long signerId = signContractRequest.getSignerId();
        String result = contractDao.getStatusByContractId(contractId);

        try {
            if (Role.user.toString().equals(role)) {
                return handleUserSigning(contractId, signerId, result);
            } else if (Role.merchant.toString().equals(role)) {
                return handleMerchantSigning(contractId, signerId, result);
            } else {
                logger.warn("Invalid role: {} for signing contract.", role);
                return "Invalid role for signing.";
            }
        } catch (Exception e) {
            logger.error("Error during signing contract: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to sign contract", e);
        }
    }

    private String handleUserSigning(Long contractId, Long signerId, String currentStatus) {
        if (ContractStatus.MERCHANTSIGN.toString().equals(currentStatus)) {
            contractDao.updateContractStatusToExecute(contractId);
            logger.info("Contract ID: {} status updated to Execute. User signed.", contractId);
            return "The contract has been executed.";
        }
        logger.info("test contractStatus: {}", ContractStatus.MERCHANTSIGN.toString());
        contractDao.updateUserSignStatus(signerId);
        logger.info("User ID: {} signed the contract successfully.", signerId);
        return "Contract signed successfully by User.";
    }

    private String handleMerchantSigning(Long contractId, Long signerId, String currentStatus) {
        if (ContractStatus.USERSIGN.toString().equals(currentStatus)) {
            contractDao.updateContractStatusToExecute(contractId);
            logger.info("Contract ID: {} status updated to Execute. Merchant signed.", contractId);
            return "The contract has been executed.";
        }
        contractDao.updateMerchantSignStatus(signerId);
        logger.info("Merchant ID: {} signed the contract successfully.", signerId);
        return "Contract signed successfully by Merchant.";
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
