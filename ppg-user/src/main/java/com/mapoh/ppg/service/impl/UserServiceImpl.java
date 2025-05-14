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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        // 判断余额是否足够
        if (balance.compareTo(amount) < 0) {
            // 余额不足，结算失败
            return false;
        }

        // 执行扣款
        BigDecimal newBalance = balance.subtract(amount);
        try {
            userDao.modifyUserBalance(userId, newBalance);
            return true;
        } catch (Exception e) {
            logger.error("用户结算失败: {}", e.getMessage());
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

    @Override
    public List<JSONObject> getAllUsers() {
        List<User> users = userDao.findAll();
        logger.info("getAllUsers: {}", users);
        List<JSONObject> result = new ArrayList<>();
        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            for(User user : users) {
                result.add(new JSONObject()
                        .fluentPut("id", user.getId())
                        .fluentPut("userName", user.getUserName())
                        .fluentPut("phoneNumber", user.getPhoneNumber())
                        .fluentPut("email", user.getEmail()));
            }
        }
        return result;
    }
}
