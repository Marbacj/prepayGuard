package com.mapoh.ppg.converter;

import com.mapoh.ppg.constants.ValidityUnit;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author mabohv
 * @date 2025/1/9 21:34
 */
@Converter
public class ValidityUnitConverter implements AttributeConverter<ValidityUnit, String> {


    @Override
    public String convertToDatabaseColumn(ValidityUnit validityUnit) {
        return validityUnit.name();
    }

    @Override
    public ValidityUnit convertToEntityAttribute(String dbData) {
        return ValidityUnit.valueOf(dbData);
    }
}
