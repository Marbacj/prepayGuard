package com.mapoh.ppg.dao;

import com.mapoh.ppg.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2024/12/30 10:44
 */

@Repository
public interface MerchantDao extends JpaRepository<Merchant, Long> {

    boolean existsByMerchantNameAndIdCardNumber(String merchantName, String idCardNumber);

    boolean existsByMerchantNameAndPassword(String merchantName, String password);

    @Modifying
    @Transactional
    @Query("update Merchant m set m.amount = m.amount + :transferAmount where m.id = :merchantId")
    void updateMerchantAmountById(@Param("merchantId")Long merchantId, @Param("transferAmount")BigDecimal transferAmount);

    @Modifying
    @Query("UPDATE Merchant m set m.amount  = m.amount+ :delta, m.version = m.version + 1 where m.id = :merchantId AND m.version = :expectedVersion")
    int casUpdateBalance(@Param("merchantId") Long merchantId,@Param("delta") BigDecimal delta,@Param("expectedVersion") int expectedVersion);

    Merchant findMerchantById(Long id);
}
