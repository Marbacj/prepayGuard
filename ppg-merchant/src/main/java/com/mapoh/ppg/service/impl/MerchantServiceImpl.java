package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.dao.MerchantDao;
import com.mapoh.ppg.service.MerchantService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2025/2/7 21:15
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Resource
    MerchantDao merchantDao;

    MerchantServiceImpl(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    @Override
    @Transactional
    public Boolean addAmountByTransfer(Long merchantId, BigDecimal transferAmount) {
        try {
            merchantDao.updateMerchantAmountById(merchantId,transferAmount);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
