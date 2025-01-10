package com.mapoh.ppg.converter;

import com.mapoh.ppg.constants.ContractStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author mabohv
 * @date 2025/1/9 21:28
 */

@Converter
public class ContractStatusConverter implements AttributeConverter<ContractStatus, Integer>{


    @Override
    public Integer convertToDatabaseColumn(ContractStatus contractStatus) {
        return contractStatus.getCode();
    }

    @Override
    public ContractStatus convertToEntityAttribute(Integer code) {
        return ContractStatus.of(code);
    }
}
