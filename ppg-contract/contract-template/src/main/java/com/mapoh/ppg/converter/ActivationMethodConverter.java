package com.mapoh.ppg.converter;

import com.mapoh.ppg.constants.ActivationMethod;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author mabohv
 * @date 2025/1/9 21:25
 */
@Converter(autoApply = true)
public class ActivationMethodConverter implements AttributeConverter<ActivationMethod, String> {

    @Override
    public String convertToDatabaseColumn(ActivationMethod attribute) {
        // 枚举值转数据库字段：存储枚举名称
        return (attribute != null) ? attribute.name() : null;
    }

    @Override
    public ActivationMethod convertToEntityAttribute(String dbData) {
        // 数据库字段转枚举值
        return (dbData != null) ? ActivationMethod.valueOf(dbData) : null;
    }
}
