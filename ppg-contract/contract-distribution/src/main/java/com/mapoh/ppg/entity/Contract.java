package com.mapoh.ppg.entity;

import com.mapoh.ppg.constants.PaymentStatus;
import com.mapoh.ppg.constants.ContractStatus;
import com.mapoh.ppg.constants.ActivationMethod;
import com.mapoh.ppg.constants.ValidityUnit;
import com.mapoh.ppg.converter.ActivationMethodConverter;
import com.mapoh.ppg.converter.ContractStatusConverter;
import com.mapoh.ppg.converter.ValidityUnitConverter;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author mabohv
 * @date 2025/1/8 19:35
 */
@Entity
@Table(name = "contracts")
@Data
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    @Column(name = "contract_code", nullable = false, unique = true)
    private Long contractCode;

    @Column(name = "unit_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitAmount;

    @Column(name = "total_units", nullable = false)
    private Integer totalUnits;

    @Column(name = "total_amount", insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "activation_method", nullable = false)
    @Convert(converter = ActivationMethodConverter.class)
    private ActivationMethod activationMethod;

    @Column(name = "activation_date")
    private LocalDate activationDate;

    @Column(name = "latest_activation_date")
    private LocalDate latestActivationDate;

    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod;

    @Column(name = "validity_unit", nullable = false)
    @Convert(converter = ValidityUnitConverter.class)
    private ValidityUnit validityUnit;

    @Column(name = "refundable")
    private Boolean refundable;

    @Column(name = "refund_fee", precision = 10, scale = 2)
    private BigDecimal refundFee;

    @Column(name = "qr_code", length = 255)
    private String qrCode;

    @Column(name = "location_info", length = 255)
    private String locationInfo;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Convert(converter = ContractStatusConverter.class)
    private ContractStatus status;

    @Column(name = "next_transfer_date")
    private LocalDateTime nextTransferDate;

    @Column(name = "transferred_units")
    private Integer transferredUnits;


    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING) // 使用字符串方式存储枚举值
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // Getters and Setters (can be generated by IDE or Lombok)
    // ...
    /** 在保存时自动设置创建时间和更新时间 */
    /** 在保存时自动设置创建时间和更新时间 */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
    }

}