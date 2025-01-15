package com.mapoh.ppg.entity;

import com.mapoh.ppg.constants.ActivationMethod;
import com.mapoh.ppg.constants.ValidityUnit;
import com.mapoh.ppg.converter.ActivationMethodConverter;
import com.mapoh.ppg.converter.ValidityUnitConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author bachmar
 * todo: add the consume function by times
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contract_templates")


public class ContractTemplate {

    /** 模板 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Integer templateId;

    /** 模板名称 */
    @Column(name = "template_name", nullable = false, length = 255)
    private String templateName;

    /** 模板描述 */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** 单位金额 */
    @Column(name = "unit_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitAmount;

    /** 有效期时长 */
    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod;

    /** 有效期单位 */
    @Column(name = "validity_unit", nullable = false, length = 10)
    @Convert(converter = ValidityUnitConverter.class)
    private ValidityUnit validityUnit;

    /** 生效方式 */
    @Column(name = "activation_method", nullable = false, length = 20)
    @Convert(converter = ActivationMethodConverter.class)
    private ActivationMethod activationMethod;

    /** 是否可退款 */
    @Column(name = "refundable", nullable = false)
    private Boolean refundable;

    /** 退款政策 */
    @Column(name = "refund_policy", columnDefinition = "TEXT")
    private String refundPolicy;

    /** 条款与条件 */
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;

    /** 模板创建时间 */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 模板更新时间 */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** 在保存时自动设置创建时间和更新时间 */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /** 在更新时自动设置更新时间 */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}