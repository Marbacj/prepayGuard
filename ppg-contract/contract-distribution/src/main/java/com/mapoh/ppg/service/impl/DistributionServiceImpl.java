package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.constants.Role;
import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.constants.ValidityUnit;
import com.mapoh.ppg.dao.ContractDao;
import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.dto.SignContractRequest;
import com.mapoh.ppg.dto.NotificationRequest;
import com.mapoh.ppg.entity.Contract;
import com.mapoh.ppg.feign.ContractTemplateFeign;
import com.mapoh.ppg.feign.MerchantFeign;
import com.mapoh.ppg.service.DistributionService;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.ContractTemplateResponse;
import com.mapoh.ppg.vo.ContractVo;
import org.bouncycastle.asn1.cms.TimeStampedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mabohv
 * @date 2025/1/10 19:46
 */

@Service
public class DistributionServiceImpl implements DistributionService {

    ContractTemplateFeign contractTemplateFeign;

    ContractDao contractDao;

    public static Logger logger = LoggerFactory.getLogger(DistributionServiceImpl.class);

    @Resource
    private final MerchantFeign merchantFeign;

    @SuppressWarnings("all")
    @Autowired
    public DistributionServiceImpl(ContractTemplateFeign contractTemplateFeign,
                                   ContractDao contractDao,
                                   MerchantFeign merchantFeign) {
        this.contractTemplateFeign = contractTemplateFeign;
        this.contractDao = contractDao;
        this.merchantFeign = merchantFeign;
    }

//    public DistributionServiceImpl(ContractDao contractDao){
//        this
//    }
    //todo: require location info
    //todo: try to acquire template id from template service
    //todo: change uuid to snowflake
    @Override
    public Long createContract(CreateContractRequest createContractRequest) {
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
        contract.setContractName(template.getTemplateName());
        // 保存合同
        contractDao.save(contract);
        NotificationRequest notificationRequest = new NotificationRequest();

        return contractCode;
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
        logger.info("status:{} for contractId:{}", result, contractId);

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

    /**
     * acquire contract amount
     * @param contractId
     * @return
     */
    @Override
    public BigDecimal getAmount(Long contractId) {
        return contractDao.getTotalAmountByContractId(contractId);
    }

    /**
     * 使合同生效
     * @param contractId
     * @return
     */
    //todo: complete the other param
    @Override
    public Boolean validateContract(Long contractId) {
        try {
            // 获取合同状态
            String status = contractDao.getStatusByContractId(contractId);
            if (!Objects.equals(status, "SIGNED")) {
                logger.info("The contract status is not expected: {}", status);
                return false;
            }

            // 获取合同的有效期单位和有效期
            ValidityUnit validityUnit = contractDao.getValidityUnitByContractId(contractId);
            Integer validityPeriod = contractDao.getValidityPeriodByContractId(contractId);

            Timestamp timestamp = transferUnitToTimestamp(validityUnit, validityPeriod);
            // 更新合同的下次转账时间
            contractDao.updateNextTransferDate(contractId, timestamp.toLocalDateTime());

            // 更新合同状态为 EXECUTE
            contractDao.updateContractStatusToExecute(contractId);

            return true;

        } catch (IllegalArgumentException e) {
            logger.error("Error while validating contract with id {}: {}", contractId, e.getMessage());
            throw new RuntimeException("Invalid validity unit or other error", e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while validating contract with id {}: {}", contractId, e.getMessage());
            return false; // 在发生未知错误时返回 false
        }
    }

    @Override
    public BigDecimal getUnitAmount(Long contractId) {
        BigDecimal unitAmount = contractDao.getUnitAmountByContractId(contractId);
        if(unitAmount == null){
            logger.info("the unitAmount is null");
            return null;
        }
        return unitAmount;
    }

    @Override
    public Long getMerchantId(Long contractId) {
        return contractDao.getMerchantIdByContractId(contractId);
    }

    @Override
    public ContractVo getContractVoByContractId(Long contractId) {

//        ValidityUnit validityUnit = contractDao.getValidityUnitByContractId(contractId);
//        Timestamp unitTime = transferUnitToTimestamp(validityUnit, 1);
//        ContractVo contractVo = contractDao.getContractVoByContractId(contractId);
//        Integer validityPeriod = contractDao.getValidityPeriodByContractId(contractId);
//        contractVo.setUnitTime(unitTime);
//        contractVo.setValidityPeriod(validityPeriod);
        Contract contract = contractDao.getContractByContractId(contractId);
        ValidityUnit validityUnit = contract.getValidityUnit();
        Timestamp unitTime = transferUnitToTimestamp(validityUnit, 1);
        ContractVo contractVo = new ContractVo();
        contractVo.setUserId(contract.getUserId());
        contractVo.setMerchantId(contract.getMerchantId());
        contractVo.setUnitTime(unitTime);
        contractVo.setUnitAmount(contract.getUnitAmount());
        contractVo.setTotalUnits(contract.getTotalUnits());
        contractVo.setTotalAmount(contract.getTotalAmount());
        contractVo.setValidityPeriod(contract.getValidityPeriod());
        return contractVo;
    }

    @Override
    public Integer getPendingOrderByMerchantId(Long merchantId) {
        Integer pendingOrder = 0;
        pendingOrder = contractDao.getPendingOrderByMerchantId(merchantId);
        return pendingOrder;
    }

    @Override
    public Integer getNewCustomer(Long merchantId) {
        return contractDao.getNewCustomer(merchantId);
    }

    @Override
    public List<JSONObject> getRelatedCustomers(Long merchantId) {

        if(merchantId == null){
            logger.info("merchantId is null:{}", merchantId);
            return Collections.emptyList();
        }
        List<JSONObject> result = new ArrayList<>();
        List<Contract> contracts = contractDao.findByMerchantId(merchantId);
        List<Long> userIds = contracts.stream()
                .map(Contract::getUserId)
                .collect(Collectors.toList());
        List<JSONObject> resultList = merchantFeign.getMerchantDetails(userIds);
        return Collections.emptyList();
    }

    @Override
    public List<JSONObject> getContractListByMerchantId(Long merchantId) {
        List<Contract> contracts = contractDao.findByMerchantId(merchantId);
        logger.info("this is contract List:{}", contracts);
        List<JSONObject> result = new ArrayList<>();
        for (Contract contract : contracts){
            result.add(new JSONObject()
                    .fluentPut("contractName", contract.getContractName())
                    .fluentPut("contractId", contract.getContractId())
                    .fluentPut("userId", contract.getUserId())
                    .fluentPut("merchantId", contract.getMerchantId())
                    .fluentPut("templateId", contract.getTemplateId())
                    .fluentPut("contractCode", contract.getContractCode())
                    .fluentPut("unitAmount", contract.getUnitAmount())
                    .fluentPut("totalUnits", contract.getTotalUnits())
                    .fluentPut("startTime", contract.getCreatedAt())
                    .fluentPut("endTime", contract.getLatestActivationDate())
                    .fluentPut("status", contract.getStatus())
            );
        }
        return result;
    }

    @Override
    public String deleteContract(Long contractId) {
        if(contractId == null)
            return "contractId is null";
        try {
            contractDao.deleteById(contractId);
            return "Contract deleted successfully.";
        } catch (Exception e) {
            logger.error("Error deleting contract with ID: {}", contractId, e);
            return "Error deleting contract.";
        }
    }

    @Override
    public List<JSONObject> getUserHistoryContractList(Long userId) {
        if(userId == null){
            logger.info("userId is null:{}", userId);
            return Collections.emptyList();
        }
        List<Contract> contracts = contractDao.findByUserId(userId);
        List<JSONObject> result = new ArrayList<>();
        for (Contract contract : contracts){
            // 计算 endTime：activationDate + validityPeriod + validityUnit
            LocalDate activationDate = contract.getActivationDate() != null
                    ? contract.getActivationDate()
                    : null;
            LocalDate endTime = null;
            if (activationDate != null && contract.getValidityPeriod() != null && contract.getValidityUnit() != null) {
                switch (contract.getValidityUnit()) {
                    case YEAR:
                        endTime = activationDate.plusYears(contract.getValidityPeriod());
                        break;
                    case MONTH:
                        endTime = activationDate.plusMonths(contract.getValidityPeriod());
                        break;
                    case WEEK:
                        endTime = activationDate.plusWeeks(contract.getValidityPeriod());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported validity unit: " + contract.getValidityUnit());
                }
            }
            result.add(new JSONObject()
                    .fluentPut("contractName", contract.getContractName())
                    .fluentPut("contractId", contract.getContractId())
                    .fluentPut("userId", contract.getUserId())
                    .fluentPut("merchantId", contract.getMerchantId())
                    .fluentPut("templateId", contract.getTemplateId())
                    .fluentPut("contractCode", contract.getContractCode())
                    .fluentPut("endTime", endTime != null ? endTime.toString() : null)
                    .fluentPut("status", contract.getStatus())
                    .fluentPut("totalAmount", contract.getTotalAmount())
            );
        }
        return result;
    }

    @Override
    public List<JSONObject> getUnpayedContractList(Long userId) {
        List<Contract> contracts = contractDao.findByUserIdAndStatus(userId, ContractStatus.SIGNED);
        List<JSONObject> result = new ArrayList<>();
        for (Contract contract : contracts) {
            result.add(new JSONObject()
                    .fluentPut("contractName", contract.getContractName())
                    .fluentPut("contractId", contract.getContractId())
                    .fluentPut("userId", contract.getUserId())
                    .fluentPut("merchantId", contract.getMerchantId())
                    .fluentPut("templateId", contract.getTemplateId())
                    .fluentPut("contractCode", contract.getContractCode())
                    .fluentPut("totalAmount", contract.getTotalAmount())
                    .fluentPut("createdAt", contract.getCreatedAt())
            );
        }
        return result;
    }

    @Override
    public Boolean changeStatusToRefund(Long contractId) {
        Boolean result = contractDao.changeStatusToRefund(contractId);
        logger.info("Contract ID: {} status updated to Refunding.", contractId);
        return result;
    }

    private String handleUserSigning(Long contractId, Long signerId, String currentStatus) {
        if (ContractStatus.MERCHANTSIGN.toString().equals(currentStatus)) {
            contractDao.updateContractStatusToSigned(contractId);
            logger.info("Contract ID: {} status updated to Execute. User signed.", contractId);
            return "The contract has been executed.";
        }
        logger.info("test contractStatus: {}", ContractStatus.MERCHANTSIGN.toString());
        contractDao.updateUserSignStatus(contractId);
        logger.info("User ID: {} signed the contract successfully.", contractId);
        return "Contract signed successfully by User.";
    }

    private String handleMerchantSigning(Long contractId, Long signerId, String currentStatus) {
        if (ContractStatus.USERSIGN.toString().equals(currentStatus)) {
            contractDao.updateContractStatusToSigned(contractId);
            logger.info("Contract ID: {} status updated to Execute. Merchant signed.", contractId);
            return "The contract has been executed.";
        }
        contractDao.updateMerchantSignStatus(contractId);
        logger.info("Merchant ID: {} signed the contract successfully.", contractId);
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

    /**
     *
     * @param validityUnit
     * @param validityPeriod
     * @return
     */
    private Timestamp transferUnitToTimestamp(ValidityUnit validityUnit, Integer validityPeriod) {

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        // 当前时间戳
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        // 根据有效期单位更新日期
        switch (validityUnit) {
            case YEAR:
                // 加上 validityPeriod 年
                calendar.add(Calendar.YEAR, validityPeriod);
                break;
            case MONTH:
                // 加上 validityPeriod 月
                calendar.add(Calendar.MONTH, validityPeriod);
                break;
            case WEEK:
                // 加上 validityPeriod 周 (假设 validityPeriod 是周数)
                calendar.add(Calendar.DAY_OF_MONTH, validityPeriod * 7);
                break;
            default:
                throw new IllegalArgumentException("Unsupported ValidityUnit: " + validityUnit);
        }

        // 获取更新后的时间戳
        timestamp = new Timestamp(calendar.getTimeInMillis());
        return timestamp;
    }

}
