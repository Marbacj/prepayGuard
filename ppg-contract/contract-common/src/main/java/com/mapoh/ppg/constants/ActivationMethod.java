package com.mapoh.ppg.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Objects;
import java.util.stream.Stream;

/** 激活日期枚举类
 * @author mabohv
 * @date 2025/1/8 21:01
 */

@Getter
@AllArgsConstructor
public enum ActivationMethod {

    FIRSTUSE("首次消费", 1),
    SPEICALDAY("具体日期", 2),
    LASTDAY("最迟生效日期", 3);


    private String description;

    private Integer code;

    public static ActivationMethod of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(ActivationMethod.values())
                .filter(bean -> bean.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid activation code: " + code));
    }
}
