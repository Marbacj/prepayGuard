package com.mapoh.ppg.dao;

import com.mapoh.ppg.entity.ContractTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mabohv
 * @date 2025/1/7 22:50
 */

@Repository
public interface ContractTemplateDao extends JpaRepository<ContractTemplate, Long> {

    public boolean existsByName(String name);
}
