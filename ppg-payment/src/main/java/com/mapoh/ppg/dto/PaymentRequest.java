package com.mapoh.ppg.dto;

/**
 * @author mabohv
 * @date 2025/1/14 20:57
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    /**
     * 订单ID（唯一标识订单）
     */
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    /**
     * 支付金额（单位：元）
     */
    @NotNull(message = "支付金额不能为空")
    private BigDecimal amount;

    /**
     * 支付渠道（如：alipay、wechat、unionpay）
     */
    @NotBlank(message = "支付渠道不能为空")
    private String paymentChannel;

    /**
     * 支付回调地址（用于接收支付结果通知）
     */
    @NotBlank(message = "回调地址不能为空")
    private String notifyUrl;

    /**
     * 支付完成后的跳转地址（如支付成功跳转页面）
     */
    private String returnUrl;

    /**
     * 订单描述（商品名称或支付说明）
     */
    private String description;

    /**
     * 扩展参数（用于额外信息传递）
     */
    private String extraParams;
}
