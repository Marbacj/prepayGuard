package com.mapoh.ppg.dto;

import com.mapoh.ppg.constants.ActivationMethod;
import com.mapoh.ppg.constants.ValidityUnit;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * @author bachmar
 */
@Data
public class TemplateRequest {

    /** 模板名称 */
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 255, message = "模板名称长度不能超过255个字符")
    private String templateName;

    /** 模板描述 */
    @Size(max = 2000, message = "模板描述长度不能超过2000个字符")
    private String description;

    /** 单位金额 */
    @NotNull(message = "单位金额不能为空")
    @DecimalMin(value = "0.00", inclusive = false, message = "单位金额必须大于0")
    @Digits(integer = 8, fraction = 2, message = "单位金额格式不正确")
    private BigDecimal unitAmount;

    /** 有效期时长 */
    @NotNull(message = "有效期时长不能为空")
    @Positive(message = "有效期时长必须为正数")
    private Integer validityPeriod;

    /** 有效期单位 */
    @NotBlank(message = "有效期单位不能为空")
    @Pattern(regexp = "周|月|年", message = "有效期单位必须是'周', '月'或'年'")
    private ValidityUnit validityUnit;

    /** 生效方式 */
    @NotBlank(message = "生效方式不能为空")
    @Pattern(regexp = "首次消费生效|具体日期|最迟生效日期", message = "生效方式必须是'首次消费生效', '具体日期'或'最迟生效日期'")
    private ActivationMethod activationMethod;

    /** 是否可退款 */
    @NotNull(message = "是否可退款不能为空")
    private Boolean refundable;

    /** 退款政策 */
    @Size(max = 2000, message = "退款政策长度不能超过2000个字符")
    private String refundPolicy;

    /** 条款与条件 */
    @Size(max = 2000, message = "条款与条件长度不能超过2000个字符")
    private String termsAndConditions;
}