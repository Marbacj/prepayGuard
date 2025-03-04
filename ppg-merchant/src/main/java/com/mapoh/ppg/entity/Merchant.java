package com.mapoh.ppg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author mabohv
 * @date 2024/12/30 10:15
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "merchants")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id", nullable = false)
    private Long id;

    @Column(name = "merchant_name", nullable = false, unique = true)
    private String merchantName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "id_card_number", nullable = false, unique = true)
    private String idCardNumber;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "version", nullable = false)
    private Integer version;

    public Merchant(String merchantName, String password, String idCardNumber, String phoneNumber) {
        this.merchantName = merchantName;
        this.password = password;
        this.idCardNumber = idCardNumber;
        this.phoneNumber = phoneNumber;
    }
}
