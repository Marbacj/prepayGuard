package com.mapoh.ppg.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mapoh.ppg.dao.UserDao;
import com.mapoh.ppg.entity.User;
import com.mapoh.ppg.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public JSONObject getUserDetailsByUsername(String username) {
        User user = userDao.getUserByUserName(username);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", user.getId());
        jsonObject.put("accountName", user.getAccountName());
        jsonObject.put("email", user.getEmail());
        jsonObject.put("phoneNumber", user.getPhoneNumber());
        jsonObject.put("balance", user.getBalance());
        return jsonObject;
    }


    @Override
    public Boolean rechargeInBalance(String accountName, BigDecimal amount) {
        int result = 0;
        result = userDao.modifyUserBalanceRecharge(accountName, amount);
        return result == 1;
    }

    @Override
    public Boolean modifyUserInfo(Long id, String accountName, String email, String phone, String password) {
        int result = 0;
        if(password == null){
            result = userDao.modifyUserInfo(id, accountName, email, phone);
            return result == 1;
        }
        result = userDao.modifyUserInfoWithPassword(id, accountName, email, phone, password);
        return result == 1;
    }
}
