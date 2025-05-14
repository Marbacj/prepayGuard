package com.mapoh.ppg.service;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.entity.Merchant;
import com.mapoh.ppg.vo.CommonResponse;
import javassist.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mabohv
 * @date 2025/2/7 21:15
 */

public interface MerchantService {

    Boolean addAmountByTransfer(Long merchantId, BigDecimal transferAmount);

    Boolean transferWithRetry(Long merchantId, BigDecimal transferAmount) throws InterruptedException, NotFoundException;

    Merchant getMerchantIdByMerchantName(String merchantName);

    Double getIncomeByMerchantId(Long merchantId);

    List<JSONObject> getMerchantList(JSONObject merchantIds);
}
