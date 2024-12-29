package com.mapoh.ppg.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mabohv
 * @date 2024/12/25 13:26
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    public CommonResponse(Integer code, String message) {

        this.code = code;
        this.message = message;

    }

    /**
     * {
     * code:200
     * message: success
     * data: null
     * }
     */
    public static <T> CommonResponse<T> successResponse(T data) {

        return new CommonResponse<T>(200, "success", data);

    }
    public static <T> CommonResponse<T> errorResponse(Integer code, String message, T data) {
        return new CommonResponse<T>(code, message, data);
    }
}
