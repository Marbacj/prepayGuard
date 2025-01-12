package com.mapoh.ppg.dao;

import com.mapoh.ppg.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mabohv
 * @date 2025/1/9 12:45
 */

@Repository
public interface ContractDao extends JpaRepository<Contract, Long> {

}
