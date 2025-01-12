package com.mapoh.ppg;

import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.dao.ContractDao;
import com.mapoh.ppg.dto.CreateContractRequest;
import com.mapoh.ppg.entity.Contract;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.feign.ContractTemplateFeign;
import com.mapoh.ppg.service.impl.DistributionServiceImpl;
import com.mapoh.ppg.vo.CommonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DistributionServiceImplTest {

    @Mock
    private ContractTemplateFeign contractTemplateFeign;

    @Mock
    private ContractDao contractDao;

    @InjectMocks
    private DistributionServiceImpl distributionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试合同创建成功
     */
    @Test
    void testCreateContract_Success() {
        // 构造请求参数
        CreateContractRequest request = new CreateContractRequest();
        request.setUserId("1");
        request.setMerchantId("100");
        request.setTemplateId("10");
        request.setTotalUnits(5);
        request.setTotalAmount(BigDecimal.valueOf(500.0));

        // 模拟模板数据
        ContractTemplate contractTemplate = new ContractTemplate();
        contractTemplate.setTemplateName("标准模板");

        CommonResponse<ContractTemplate> mockResponse = new CommonResponse<>();
        mockResponse.setData(contractTemplate);

        // Mock Feign 返回模板
        when(contractTemplateFeign.getContractTemplate(10)).thenReturn(mockResponse);

        // Mock 合同保存
        Contract savedContract = new Contract();
        savedContract.setContractId(123L);
        when(contractDao.save(any(Contract.class))).thenReturn(savedContract);

        // 调用方法
        String result = distributionService.createContract(request);

        // 验证结果
        assertEquals("合同创建成功，合同ID：123", result);

        // 验证方法调用
        verify(contractTemplateFeign, times(1)).getContractTemplate(10);
        verify(contractDao, times(1)).save(any(Contract.class));
    }

    /**
     * 测试获取模板失败时抛出异常
     */
    @Test
    void testCreateContract_TemplateNotFound() {
        // 构造请求参数
        CreateContractRequest request = new CreateContractRequest();
        request.setUserId("1");
        request.setMerchantId("100");
        request.setTemplateId("10");

        // Mock Feign 返回空数据
        CommonResponse<ContractTemplate> mockResponse = new CommonResponse<>();
        mockResponse.setData(null);
        when(contractTemplateFeign.getContractTemplate(10)).thenReturn(mockResponse);

        // 调用方法并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            distributionService.createContract(request);
        });

        assertEquals("获取模板失败", exception.getMessage());

        // 验证合同未被保存
        verify(contractDao, never()).save(any(Contract.class));
    }
}