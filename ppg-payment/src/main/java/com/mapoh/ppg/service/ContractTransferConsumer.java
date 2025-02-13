package com.mapoh.ppg.service;

import com.mapoh.ppg.dao.TransactionDao;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import com.mapoh.ppg.dto.payment.TransferRequest;
import com.mapoh.ppg.entity.Transaction;
import com.mapoh.ppg.feign.ContractServiceFeign;
import com.mapoh.ppg.feign.MerchantServiceFeign;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    public TransactionDao transactionDao;

    @SuppressWarnings("all")
    public ContractTransferConsumer(ContractServiceFeign contractServiceFeign, MerchantServiceFeign merchantServiceFeign) {
        this.contractServiceFeign = contractServiceFeign;
        this.merchantServiceFeign = merchantServiceFeign;
    }

    @KafkaListener(topics = "contract-scheduled", groupId = "contract-group")
    public void handleContractTransfer(ConsumerRecord<String, ContractScheduledRequest> record) {
        ContractScheduledRequest contractScheduledRequest = record.value();
        Long contractId = contractScheduledRequest.getContractId();
        Long merchantId = contractScheduledRequest.getMerchantId();
        Long userId = contractScheduledRequest.getUserId();

        logger.info("process contract transfer record: merchantId = {}, contractId = {} ", merchantId, contractId);

        BigDecimal unitAmount = contractServiceFeign.getUnitAmount(contractId).getData();

        if(unitAmount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("No valid amount retrieved for contractId: {}", contractId);
            return;
        }

        logger.info("contract:{} need to transfer every unit for amount:{}", contractId, unitAmount);
        Transaction transaction = transactionDao.getTransactionByContractId(contractId);
        transaction.setContractId(contractId);
        transaction.setUserId(userId);
        transaction.setMerchantId(merchantId);
        try {
            merchantServiceFeign.receiveTransferAccount(new TransferRequest(merchantId, unitAmount));
            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            transactionDao.insertOrUpdateTransactionSuccess(transaction);
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transactionDao.insertOrUpdateTransactionFail(transaction);
            throw new RuntimeException(e);
        }
        logger.info("contract: contractId:{} transfer success", contractId);
    }
}
