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
 * @date 2024/12/25 16:39
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
/**
 * CREATE TABLE users(
 *        user_name VARCHAR(50) NOT NULL,
 *        password VARCHAR(20) NOT NULL ,
 *        id_card_number VARCHAR(18) NOT NULL,
 *        phone_number VARCHAR(11) NOT NULL,
 *        account_name VARCHAR(20) NOT NULL,
 *        email VARCHAR(50),
 *        balance DECIMAL(10,2) DEFAULT 0,
 *        create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
 *        UNIQUE (user_name, id_card_number)
 * )
 */
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "id_card_number", nullable = false, unique = true)
    private String idCardNumber;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "email")
    private String email;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    public User(String userName, String password, String idCardNumber, String phoneNumber, String accountName, String email) {
        this.userName = userName;
        this.password = password;
        this.idCardNumber = idCardNumber;
        this.phoneNumber = phoneNumber;
        this.accountName = accountName;
        this.email = email;
    }
    // Getters and Setters
}