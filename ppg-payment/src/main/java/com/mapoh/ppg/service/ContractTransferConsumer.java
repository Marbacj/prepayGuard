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
import org.springframework.scheduling.annotation.Async;
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

    /**
     * 异步处理提高消费速度
     * @param record
     */
    @KafkaListener(topics = "contract-scheduled", groupId = "contract-group")
    public void handleContractTransfer(ConsumerRecord<String, ContractScheduledRequest> record) {
        ContractScheduledRequest contractScheduledRequest = record.value();
        Long contractId = contractScheduledRequest.getContractId();
        Long merchantId = contractScheduledRequest.getMerchantId();
        Long userId = contractScheduledRequest.getUserId();

        logger.info("Processing contract transfer record: merchantId = {}, contractId = {} ", merchantId, contractId);

        BigDecimal unitAmount = contractServiceFeign.getUnitAmount(contractId).getData();
        if (unitAmount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("No valid amount retrieved for contractId: {}", contractId);
            return;
        }
        logger.info("Processing transfer for contract:{} - user:{} merchant:{} unitAmount:{}",
                contractId, userId, merchantId, unitAmount);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setMerchantId(merchantId);
        transaction.setContractId(contractId);
        transaction.setAmount(unitAmount);

        // 异步处理
        processTransferAsync(contractId, merchantId, unitAmount, transaction);

        logger.debug("Completed processing for contract:{}", contractId);
    }

    @Async
    public void processTransferAsync(Long contractId, Long merchantId, BigDecimal unitAmount, Transaction transaction) {
        try {
            logger.debug("Initiating transfer to merchant:{}, amount:{}", merchantId, unitAmount);
            merchantServiceFeign.receiveTransferAccount(new TransferRequest(merchantId, unitAmount));

            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            transactionDao.insertOrUpdateTransactionSuccess(transaction);
            logger.info("Transfer SUCCESS - contract:{} merchant:{} amount:{}", contractId, merchantId, unitAmount);
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transactionDao.insertOrUpdateTransactionFail(transaction);
            String errorMsg = String.format("Transfer FAILED - contract:%s merchant:%s amount:%s", contractId, merchantId, unitAmount);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }


    @KafkaListener(topics = "contract-scheduled-failed", groupId = "contract-group-retries")
    public void handleContractTransferFailed(ConsumerRecord<String, Transaction> record) {
        logger.info("the retries for compensation transaction:{}",record.value().getContractId());

        Transaction transaction = record.value();
        Long contractId = transaction.getContractId();
        Long merchantId = transaction.getMerchantId();
        BigDecimal unitAmount = transaction.getAmount();
        try {
            merchantServiceFeign.receiveTransferAccount(new TransferRequest(merchantId, unitAmount));
            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
            transactionDao.insertOrUpdateTransactionSuccess(transaction);
            logger.info("the compensation transaction success:{}",record.value().getContractId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
