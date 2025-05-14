package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dao.RefundDao;
import com.mapoh.ppg.entity.Refund;
import com.mapoh.ppg.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mabohv
 * @date 2025/2/16 21:44
 */

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    RefundDao refundDao;

    public final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);



    AdminServiceImpl(RefundDao refundDao) {
        this.refundDao = refundDao;
    }
    /**
     * 查询退款列表
     * @param page
     * @param size
     * @param status
     * @return
     */
    @Override
    public List<JSONObject> getRefundList(int page, int size, String status) {
        List<Refund> refundList = new ArrayList<>();
        int offset = page*size;
        refundList = refundDao.getRefundListByStatus(offset, size, status);

        List<JSONObject> ansList = new ArrayList<>();
        for(Refund refund : refundList) {
            JSONObject ans = new JSONObject();
            ans.put("id", refund.getId());
            ans.put("status", refund.getStatus());
            ans.put("contractId",refund.getContract());
            ans.put("userId", refund.getUser());
            ans.put("created_time",refund.getCreatedTime());
            ans.put("updated_time",refund.getUpdatedTime());
            ans.put("reason", refund.getReason());
            ansList.add(ans);
            logger.info(ans.toJSONString());
        }
        return ansList;
    }

    @Override
    public JSONObject getRefundRecord(Long refundId) {
        JSONObject ans = refundDao.findById(refundId)
                .map(this::convertToJSONObject)
                .orElse(null);
        logger.info(ans.toJSONString());
        return ans;
    }

    /**
     *
     * 过期需要考虑的
     * 首先要让把剩余的合同周期跑完：
     * 其次需要终止后面的延时任务
     * @param refundId
     * @param status
     * @param reason
     * @return
     * public void cancelContractWithCurrentInstallment(String contractId, int currentInstallment, long remainingTimeMillis) {
     *     String cancelKey = "contract:cancel:" + contractId;
     *
     *     // 存储当前期数到 Redis，并设置剩余时间后过期
     *     redisTemplate.opsForValue().set(cancelKey, String.valueOf(currentInstallment));
     *     redisTemplate.expire(cancelKey, remainingTimeMillis, TimeUnit.MILLISECONDS);
     *
     *     logger.info("合同 {} 已标记取消，当前期 {} 结束后将移除后续任务", contractId, currentInstallment);
     * }
     */
    @Override
    public Boolean processRefund(Long refundId, String status, String reason) {
        return Boolean.FALSE;

    }

    private JSONObject convertToJSONObject(Refund refund) {
        JSONObject ans = new JSONObject();
        ans.put("id", refund.getId());
        ans.put("status", refund.getStatus());
        ans.put("contractId",refund.getContract());
        ans.put("userId", refund.getUser());
        ans.put("created_time",refund.getCreatedTime());
        ans.put("updated_time",refund.getUpdatedTime());
        ans.put("reason", refund.getReason());
        return ans;
    }

}
