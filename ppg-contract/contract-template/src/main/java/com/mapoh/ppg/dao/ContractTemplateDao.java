package com.mapoh.ppg.dao;

import com.mapoh.ppg.entity.ContractTemplate;
import com.netflix.ribbon.proxy.annotation.TemplateName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author mabohv
 * @date 2025/1/7 22:50
 */

@Repository
public interface ContractTemplateDao extends JpaRepository<ContractTemplate, Long> {

    public boolean existsByTemplateName(String templateName);

    public Optional<ContractTemplate> findContractTemplateByTemplateId(Integer templateId);

    // 通过ID查询模板名称（推荐）
    @Query("SELECT c.templateName FROM ContractTemplate c WHERE c.templateId = :templateId")
    String findTemplateNameByTemplateId(@Param("templateId") Integer templateId);

    public Optional<ContractTemplate> getContractTemplateByTemplateId(Integer contractId);

    List<ContractTemplate> findContractTemplateByMerchantId(Long merchantId);
}
