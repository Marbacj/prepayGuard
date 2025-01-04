package com.mapoh.ppg.dao;

import com.mapoh.ppg.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mabohv
 * @date 2024/12/30 10:44
 */

@Repository
public interface MerchantDao extends JpaRepository<Merchant, Long> {

    boolean existsByMerchantNameAndIdCardNumber(String merchantName, String idCardNumber);

    boolean existsByMerchantNameAndPassword(String merchantName, String password);
}
