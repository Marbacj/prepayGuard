package com.mapoh.ppg.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author mabohv
 * @date 2025/1/8 21:29
 */

@Getter
@AllArgsConstructor
public enum ValidityUnit {

    WEEK("周",1),
    MONTH("月", 2),
    YEAR("年", 3);


    private String description;

    private Integer code;

    public static ValidityUnit getByCode(Integer code) {
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid validity unit code: %s", code)));
    }
}
