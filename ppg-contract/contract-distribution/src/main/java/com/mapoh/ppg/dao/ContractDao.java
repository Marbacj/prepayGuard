package com.mapoh.ppg.dao;

import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.entity.Contract;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/1/9 12:45
 */

@Repository
public interface ContractDao extends JpaRepository<Contract, Long> {

    @Query("SELECT c.status from Contract c where c.contractId = :contractId")
    String getStatusByContractId(@Param("contractId") Long contractId);

    @Modifying
    @Query("UPDATE Contract c SET c.status = 'SIGNED' WHERE c.contractId = :contractId")
    void updateContractStatusToSigned(@Param("contractId") Long contractId);


    @Modifying
    @Query("UPDATE Contract c SET c.status = 'USERSIGN' WHERE c.userId = :signerId")
    void updateUserSignStatus(@Param("signerId") Long signerId);

    @Modifying
    @Query("UPDATE Contract c SET c.status = 'MERCHANTSIGN' WHERE c.merchantId = :signerId")
    void updateMerchantSignStatus(@Param("signerId") Long signerId);

    @Query("SELECT c.totalAmount from Contract c where c.contractId = :contractId")
    BigDecimal getTotalAmountByContractId(@Param("contractId") Long contractId);

    @Modifying
    @Query("UPDATE Contract c SET c.status = 'EXECUTE' WHERE c.contractId = :contractId")
    void updateContractStatusToExecute(@Param("contractId") Long contractId);
}
