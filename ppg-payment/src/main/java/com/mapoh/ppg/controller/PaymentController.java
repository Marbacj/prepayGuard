package com.mapoh.ppg.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapoh.ppg.constants.Status;
import com.mapoh.ppg.dto.BalancePaymentRequest;
import com.mapoh.ppg.dto.RefundRequest;
import com.mapoh.ppg.service.PayStrategy;
import com.mapoh.ppg.service.PaymentService;
import com.mapoh.ppg.service.adapter.PaymentChannelAdapter;
import com.mapoh.ppg.service.adapter.PaypalAdapter;
import com.mapoh.ppg.service.impl.PaypalPayment;
import com.mapoh.ppg.service.strategy.PayContextStrategy;
import com.mapoh.ppg.vo.CommonResponse;
import com.mapoh.ppg.vo.OnlinePaymentResponse;
import com.paypal.sdk.PaypalServerSDKClient;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static org.bouncycastle.crypto.tls.ConnectionEnd.client;

/**
 * @author mabohv
 * @date 2025/1/14 20:47
 * 用户进行支付
 * 1. 确定支付方式
 * 2. 将金额全部转到托管平台
 * 3. 如果是零钱支付，就进行死信队列来进行完成任务
 * 4. 如果是其他支付方式，那么就转到管理财务表
 */

@RestController
public class PaymentController {

    private final static Logger logger = LoggerFactory.getLogger(PaymentController.class);

    PaymentService paymentService;
    private final PaypalServerSDKClient paypalClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public PaymentController(PaymentService paymentService,
                             PaypalServerSDKClient paypalClient,
                             ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.paypalClient = paypalClient;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/pay/balance")
    public CommonResponse<Boolean> payInBalance(@RequestBody BalancePaymentRequest balancePaymentRequest) {
        Long contractId = balancePaymentRequest.getContractId();
        Long userId = balancePaymentRequest.getUserId();

        if(userId == null || contractId == null) {
            return CommonResponse.successResponse(false);
        }

        return CommonResponse.successResponse(paymentService.payInBalance(balancePaymentRequest));
    }

//    @PostMapping("pay/paypal")
//    public CommonResponse<Boolean> payInpaypal(@RequestBody BalancePaymentRequest balancePaymentRequest) {
//        Long contractId = balancePaymentRequest.getContractId();
//        Long userId = balancePaymentRequest.getUserId();
//
//        if(userId == null || contractId == null) {
//            return CommonResponse.successResponse(false);
//        }
//        PaymentChannelAdapter payment = new PaypalAdapter(new PaypalPayment());
//        payment.pay(contractId, userId);
//        return CommonResponse.successResponse(true);
//    }

    @PostMapping("/refund")
    public CommonResponse<Boolean> refund(@RequestBody RefundRequest refundRequest) {
        if(refundRequest == null || refundRequest.getContractId() == null || refundRequest.getUserId() == null) {
            return CommonResponse.successResponse(false);
        }
        return CommonResponse.successResponse(paymentService.refundBalance(refundRequest));
    }

    /**
     *
     * {
     *     "contract":{
     *         id:""
     *         amount:"",
     *         userID: "",
     *     }
     * }
     * todo: abstract the main body to the paypalStrategy
     * @param request
     * @return
     */
    @PostMapping("/api/orders")
    public CommonResponse<Order> createOrder(@RequestBody JSONObject request) throws IOException, ApiException {
        // 获取合同ID（如果有需要）
        Long contractId = request.getLong("contractId");
        // 获取金额
        BigDecimal amount = request.getBigDecimal("amount");

        // 调用创建订单的业务方法
        Order order = createOrder(amount);
//        PayContextStrategy payContextStrategy = new PayContextStrategy();
//        boolean success = payContextStrategy.pay("10001", amount);
//        if(success) {
//            logger.info("payment of {} was successful");
//        }

        // 返回订单信息
        return CommonResponse.successResponse(order);
    }

    public Order createOrder(BigDecimal amount) throws IOException, ApiException {
        // 构造订单创建请求
        OrdersCreateInput ordersCreateInput = new OrdersCreateInput.Builder(
                null,
                new OrderRequest.Builder(
                        CheckoutPaymentIntent.CAPTURE,
                        Arrays.asList(
                                new PurchaseUnitRequest.Builder(
                                        new AmountWithBreakdown.Builder(
                                                "USD",
                                                amount.toString())
                                                .build())
                                        .build()))
                        .build())
                .build();

        // 调用 PayPal 的 API 创建订单
        OrdersController ordersController = paypalClient.getOrdersController();
        ApiResponse<Order> apiResponse = ordersController.ordersCreate(ordersCreateInput);

        // 返回创建的订单
        return apiResponse.getResult();
    }


    @PostMapping("/api/orders/{orderID}/capture")
    public ResponseEntity<Order> captureOrder(@PathVariable String orderID) {
        try {
            Order response = captureOrders(orderID);
            return new ResponseEntity<Order>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Order captureOrders(String orderID) throws IOException, ApiException {
        OrdersCaptureInput ordersCaptureInput = new OrdersCaptureInput.Builder(
                orderID,
                null)
                .build();
        OrdersController ordersController = paypalClient.getOrdersController();
        ApiResponse<Order> apiResponse = ordersController.ordersCapture(ordersCaptureInput);
        return apiResponse.getResult();
    }
}
