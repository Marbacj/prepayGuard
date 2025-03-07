package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.dao.UserDao;
import com.mapoh.ppg.dto.UserInfoRequest;
import com.mapoh.ppg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * @author mabohv
 * @date 2024/12/27 18:45
 */

@Service
public class UserServiceImpl implements UserService {

    UserDao userDao;

    public Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * todo: 可以在提交之前进行检查，防止重复提交
     * @param userInfoRequest
     * @return
     */
    @Transactional
    @Override
    public boolean modifyUserInfo(UserInfoRequest userInfoRequest, Long id) {
        if (userInfoRequest == null) {
            logger.warn("Empty request for user update");
            return false; // 或者抛出异常
        }

        // 获取用户输入的信息
        String password = userInfoRequest.getPassword();
        String phoneNumber = userInfoRequest.getPhoneNumber();
        String email = userInfoRequest.getEmail();
        String accountName = userInfoRequest.getAccountName();

        logger.info("Updating user info for ID: {}", id);

        // 调用 DAO 层方法
        int changedCount = userDao.modifyUserInfo(id, accountName, email, password, phoneNumber);

        // 返回操作结果
        boolean isSuccess = changedCount > 0;
        logger.info("User update {} for ID: {}", isSuccess ? "succeeded" : "failed", id);

        return isSuccess;
    }

    @Override
    public BigDecimal getUserBalance(Long userId) {
        return userDao.getBalance(userId);
    }

    @Override
    public boolean settlement(Long userId, BigDecimal amount) {
        BigDecimal balance = userDao.getBalance(userId);

        boolean result = balance.compareTo(amount) > 0;

        BigDecimal sub = balance.subtract(amount);

        try {
            if (result) {
                userDao.modifyUserBalance(userId, sub);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

    }
}
