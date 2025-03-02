package com.mapoh.ppg.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author mabohv
 * @date 2025/3/2 10:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refunds")
public class Refund {
    // 退款ID（主键）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 关联的用户
    @Column(name = "user_id")
    private Long user;
    // 关联的合同
    @Column(name = "contract_id")
    private Long contract;
    // 退款金额
    @Column(nullable = false)
    private BigDecimal amount;
    // 退款状态: PENDING, APPROVED, REJECTED
    @Column(nullable = false, length = 20)
    private String status;
    // 退款原因
    @Column(length = 255)
    private String reason;
    // 申请时间
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
    // 更新时间
    @Column(name = "updated_time")
    private LocalDateTime updatedTime = LocalDateTime.now();
    // 更新前自动修改更新时间
    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}
