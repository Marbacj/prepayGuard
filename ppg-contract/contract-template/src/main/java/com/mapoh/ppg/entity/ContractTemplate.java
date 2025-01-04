package com.mapoh.ppg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author mabohv
 * @date 2024/1/4 10:00:00
 */

@Entity
@Table(name = "contract_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractTemplate {

    /** 模板 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer templateId;

    /** 模板名称 */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /** 模板描述 */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** 模板相关字段配置（JSON 格式） */
    @Column(name = "fields", nullable = false, columnDefinition = "JSON")
    private String fields;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}