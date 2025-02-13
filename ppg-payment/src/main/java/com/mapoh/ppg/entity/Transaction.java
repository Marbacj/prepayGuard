package com.mapoh.ppg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author mabohv
 * @date 2025/2/12 18:43
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_log")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;


    // Getters and Setters

    public enum TransactionStatus {
        PENDING,
        SUCCESS,
        FAILED,
        RETRY
    }

    public Transaction(Long contractId, Long merchantId, Long userId, TransactionStatus status) {
        this.contractId = contractId;
        this.merchantId = merchantId;
        this.userId = userId;
        this.status = status;
    }
}