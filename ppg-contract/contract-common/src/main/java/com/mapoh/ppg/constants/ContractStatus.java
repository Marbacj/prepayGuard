package com.mapoh.ppg.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author mabohv
 * @date 2025/1/8 21:46
 */

@Getter
@AllArgsConstructor
public enum ContractStatus {

    READY("待签订",1),
    USERSIGN("用户已签署", 2),
    MERCHANTSIGN("商家已签署", 3),
    SIGNED("合同已经签署", 4),
    EXECUTE("执行中",5),
    ACCOMPLISH("已完成",6),
    CANCELLED("已取消",7);

    private String description;

    private Integer code;

    public static ContractStatus of(Integer code) {
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("not exist contract status"));
    }
}
