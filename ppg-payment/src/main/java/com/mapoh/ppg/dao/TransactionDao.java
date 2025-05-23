package com.mapoh.ppg.dao;

import com.mapoh.ppg.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author mabohv
 * @date 2025/2/12 18:52
 */

@Repository
public interface TransactionDao extends JpaRepository<Transaction, Long> {

//    @Modifying
//    @Query(value = "INSERT INTO Transaction (contractId, merchantId, userId, status) " +
//            "VALUES (:contractId, :merchantId, :userId, 'SUCCESS') " +
//            "ON DUPLICATE KEY UPDATE merchantId = :merchantId, userId = :userId, status = 'SUCCESS'", nativeQuery = true)
//    void insertOrUpdateTransactionSuccess(
//            @Param("contractId") Long contractId,
//            @Param("merchantId") Long merchantId,
//            @Param("userId") Long userId
//    );
//
//    @Modifying
//    @Query(value = "INSERT INTO Transaction (contractId, merchantId, userId, status) " +
//            "VALUES (:contractId, :merchantId, :userId, 'FAILED') " +
//            "ON DUPLICATE KEY UPDATE merchantId = :merchantId, userId = :userId, status = 'FAILED'", nativeQuery = true)
//    void insertOrUpdateTransactionFail(
//            @Param("contractId") Long contractId,
//            @Param("merchantId") Long merchantId,
//            @Param("userId") Long userId
//    );
    @Transactional
    default void insertOrUpdateTransactionSuccess(Transaction transaction) {
        save(transaction);
    }

    @Transactional
    default void insertOrUpdateTransactionFail(Transaction transaction) {
        save(transaction);
    }

    @Query("SELECT t.transactionId from Transaction t")
    List<Long> getTransactionIdsByStatus(String status);

    Transaction getTransactionByContractId(Long contractId);

    List<Transaction> getTransactionsByStatus(Transaction.TransactionStatus transactionStatus);

    @Modifying
    @Query("UPDATE Transaction t set t.retryCount = t.retryCount + 1")
    void updateRetryCount(Transaction transaction);

    @Query("SELECT t FROM Transaction t WHERE t.status = 'FAILED' AND MOD(t.transactionId, :shardTotal) = :shardIndex")
    List<Transaction> getFailedTransactionsByShard(
            @Param("shardIndex") int shardIndex,
            @Param("shardTotal") int shardTotal
    );

    @Query("SELECT CASE WHEN t.status = 'success' THEN false ELSE true END FROM Transaction t WHERE t.transactionId = :transactionId")
    Boolean checkStatusByTransactionId(@Param("transactionId") Long transactionId);


    @Query("SELECT SUM(t.amount) " +
            "FROM Transaction t " +
            "WHERE t.merchantId = :merchantId " +
            "AND t.status = 'SUCCESS' " +
            "AND FUNCTION('DATE', t.transactionDate) = CURRENT_DATE " +
            "GROUP BY FUNCTION('DATE', t.transactionDate)")
    Double getIncomeByMerchantId(Long merchantId);
}