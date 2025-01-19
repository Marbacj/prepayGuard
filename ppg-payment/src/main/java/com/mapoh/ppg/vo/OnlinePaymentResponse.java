package com.mapoh.ppg.vo;

/**
 * @author mabohv
 * @date 2025/1/14 20:55
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlinePaymentResponse {

    /**
     * 订单ID（与请求保持一致）
     */
    private String orderId;

    /**
     * 支付状态（SUCCESS：支付成功，FAILED：支付失败，PENDING：待支付）
     */
    private String status;

    /**
     * 支付渠道（如：alipay、wechat）
     */
    private String paymentChannel;

    /**
     * 第三方支付单号（便于对账和追踪）
     */
    private String transactionId;

    /**
     * 支付跳转链接（二维码链接或支付页面URL）
     */
    private String paymentUrl;

    /**
     * 提示信息（用于前端展示）
     */
    private String message;

    /**
     * 返回时间戳（接口响应时间）
     */
    private long timestamp;
}