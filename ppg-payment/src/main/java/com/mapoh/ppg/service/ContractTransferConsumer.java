package com.mapoh.ppg.service;

import com.mapoh.ppg.dto.ContractScheduledRequest;
import com.mapoh.ppg.dto.payment.TransferRequest;
import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.feign.MerchantServiceFeign;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/7 10:01
 */

@Service
public class ContractTransferConsumer {

    public static Logger logger = LoggerFactory.getLogger(ContractTransferConsumer.class);

    public ContractServiceFeign contractServiceFeign;
    public MerchantServiceFeign merchantServiceFeign;

    @SuppressWarnings("all")
    @Autowired
    public ContractTransferConsumer(ContractServiceFeign contractServiceFeign, MerchantServiceFeign merchantServiceFeign) {
        this.contractServiceFeign = contractServiceFeign;
        this.merchantServiceFeign = merchantServiceFeign;
    }

    @KafkaListener(topics = "contract-scheduled", groupId = "contract-group")
    public void handleContractTransfer(ConsumerRecord<String, ContractScheduledRequest> record) {
        ContractScheduledRequest contractScheduledRequest = record.value();
        Long contractId = contractScheduledRequest.getContractId();
        Long merchantId = contractScheduledRequest.getMerchantId();

        logger.info("process contract transfer record: merchantId = {}, contractId = {} ", merchantId, contractId);

        BigDecimal unitAmount = contractServiceFeign.getUnitAmount(contractId).getData();

        if(unitAmount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("No valid amount retrieved for contractId: {}", contractId);
            return;
        }
        logger.info("contract:{} need to transfer every unit for amount:{}", contractId, unitAmount);

        merchantServiceFeign.receiveTransferAccount(new TransferRequest(merchantId, unitAmount));
        logger.info("contract: contractId:{} transfer success", contractId);
    }
}
