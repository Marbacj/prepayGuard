package com.mapoh.ppg.advice;

import com.mapoh.ppg.annotation.IgnoreResponseAdvice;
import com.mapoh.ppg.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author mabohv
 * @date 2024/12/25 13:24
 */

@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     *
     * 判断是否需要对于响应进行处理
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if(methodParameter.getDeclaringClass().isAssignableFrom(IgnoreResponseAdvice.class)){
            return false;
        }

        if(methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }

        return true;
    }

    /**
     * 用于响应返回前进行处理
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        //先进行定义一个最终的返回的对象
        CommonResponse<Object> commonResponse = new CommonResponse<>(0,"");
        /**
         * 如果响应体为空，那么不需要进行处理
         * 如果响应体是commonResponse的实例，直接进行填充
         * 否则就设置数据部分为data
         */
        if(o == null){
            return commonResponse;
        } else if(o instanceof CommonResponse){
            commonResponse = (CommonResponse<Object>)o;
        } else {
            commonResponse.setData(o);
        }

        return commonResponse;
    }
}
