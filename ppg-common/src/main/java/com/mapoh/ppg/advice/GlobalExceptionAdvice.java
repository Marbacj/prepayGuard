package com.mapoh.ppg.advice;

import com.mapoh.ppg.exception.CommException;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mabohv
 * @date 2024/12/25 13:43
 */

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(value = CommException.class)
    public CommonResponse<String> handlerException(
            HttpServletRequest request, CommException e
    ){
        CommonResponse<String> commonResponse = new CommonResponse<>(-1, "bussiness error");
        commonResponse.setData(e.getMessage());
        return commonResponse;
    }
}
