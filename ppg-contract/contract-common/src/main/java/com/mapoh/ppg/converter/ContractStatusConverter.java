package com.mapoh.ppg.converter;

import com.mapoh.ppg.constants.ContractStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author mabohv
 * @date 2025/1/9 21:28
 */

@Converter(autoApply = true)
public class ContractStatusConverter implements AttributeConverter<ContractStatus, String>{


    @Override
    public String convertToDatabaseColumn(ContractStatus contractStatus) {
        return contractStatus == null ? null : contractStatus.name();
    }

    @Override
    public ContractStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ContractStatus.valueOf(dbData);
    }
}
