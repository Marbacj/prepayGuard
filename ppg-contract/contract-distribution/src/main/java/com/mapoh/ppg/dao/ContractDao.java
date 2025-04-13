package com.mapoh.ppg.dao;

import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.constants.ValidityUnit;
import com.mapoh.ppg.entity.Contract;
import com.mapoh.ppg.vo.ContractVo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    @Transactional
    @Query("UPDATE Contract c SET c.status = 'EXECUTE', c.paymentStatus = 'PARTIAL_PAID' WHERE c.contractId = :contractId")
    void updateContractStatusToExecute(@Param("contractId") Long contractId);

    @Query("SELECT c.unitAmount from Contract c WHERE c.contractId = :contractId")
    BigDecimal getUnitAmountByContractId(@Param("contractId") Long contractId);

    @Query("SELECT c.validityPeriod from Contract c where c.contractId = :contractId")
    Integer getValidityPeriodByContractId(@Param("contractId") Long contractId);

    @Query("SELECT c.validityUnit from Contract c where c.contractId = :contractId")
    ValidityUnit getValidityUnitByContractId(@Param("contractId") Long contractId);

    @Modifying
    @Transactional
    @Query("update Contract c SET c.nextTransferDate = :timestamp where c.contractId = :contractId")
    void updateNextTransferDate(@Param("contractId") Long contractId, @Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT c.merchantId from Contract c where c.contractId = :contractId")
    Long getMerchantIdByContractId(Long contractId);

    @Query("SELECT c.userId, " +
            "c.merchantId," +
            "c.unitAmount," +
            "c.totalAmount," +
            "c.totalUnits from Contract c where c.contractId = :contractId")
    public ContractVo getContractVoByContractId(@Param("contractId") Long contractId);


    @Query("SELECT count(c.contractId) from Contract c where c.merchantId = :merchantId and c.paymentStatus = 'PENDING' ")
    Integer getPendingOrderByMerchantId(Long merchantId);
}
