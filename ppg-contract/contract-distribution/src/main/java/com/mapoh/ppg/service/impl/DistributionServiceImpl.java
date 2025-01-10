package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.dao.ContractDao;
import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.entity.Contract;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.feign.ContractTemplateFeign;
import com.mapoh.ppg.service.DistributionService;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;

/**
 * @author mabohv
 * @date 2025/1/10 19:46
 */

@Service
public class DistributionServiceImpl implements DistributionService {

    ContractTemplateFeign contractTemplateFeign;

    ContractDao contractDao;

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
        String templateId = createContractRequest.getTemplateId();

        Contract contract = new Contract();

        // 调用 Feign 客户端获取模板
        CommonResponse<ContractTemplate> response = contractTemplateFeign.getContractTemplate(Integer.valueOf(templateId));
        if (response.getData() == null) {
            throw new RuntimeException("获取模板失败");
        }

        ContractTemplate contractTemplate = response.getData();

        // 通过反射将模板内容赋值给合同对象
        Field[] fields = ContractTemplate.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(contractTemplate);
                Field contractField = Contract.class.getDeclaredField(field.getName());
                contractField.setAccessible(true);
                contractField.set(contract, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // 设置基本信息
        contract.setUserId(Long.valueOf(userId));
        contract.setMerchantId(Long.valueOf(merchantId));
        contract.setTemplateId(Integer.valueOf(templateId));
        contract.setStatus(ContractStatus.READY);
        contract.setTotalUnits(createContractRequest.getTotalUnits());
        contract.setTotalAmount(createContractRequest.getTotalAmount());
        LocalDate now = LocalDate.now();
        contract.setActivationDate(now);
        // 保存合同
        contractDao.save(contract);

        return "合同创建成功，合同ID：" + contract.getContractId();
    }
}
