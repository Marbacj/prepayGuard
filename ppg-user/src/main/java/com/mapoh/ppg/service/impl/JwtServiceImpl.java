package com.mapoh.ppg.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.mapoh.ppg.dao.UserDao;
import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.service.JwtService;
import com.mapoh.ppg.util.JwtUtil;
import com.mapoh.ppg.util.RedisUtil;
import com.mapoh.ppg.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author mabohv
 * @date 2024/12/25 16:15
 */

@Service
public class JwtServiceImpl implements JwtService {

    UserDao userDao;

    private Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    private static int expireTime = 60 * 60 * 1000;

    public void setExpireTime(int expireTime) {
        JwtServiceImpl.expireTime = expireTime * 60 * 1000;
    }

    @Autowired
    public JwtServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean register(RegisterRequest registerRequest) {

        String name = registerRequest.getUserName();
        String idCardNumber = registerRequest.getIdCardNumber();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();
        String phoneNumber = registerRequest.getPhoneNumber();
        String accountName = registerRequest.getAccountName();

        if (userDao.existsByUserNameAndIdCardNumber(name, idCardNumber)) {
            logger.warn("Register failed: userName={} and idCardNumber={} already exist", name, idCardNumber);
            return false;
        }

        userDao.save(new User(
                name, password, idCardNumber, phoneNumber, accountName,email
        ));
        logger.info("Register success: userName={}, idCardNumber={}", name, idCardNumber);
        return true;
    }

    @Override
    public String login(String username, String password) {
        if (!userDao.existsByUserNameAndPassword(username, password)) {
            logger.info("登录失败：用户名或密码错误");
            return null;
        }
        return generateNewJwt(username);
    }

    @Override
    public boolean checkJwt(String token) {

        try {
            String secret = RedisUtil.get(token);
            logger.info("checkJwt: secret={}", secret);
            if (secret == null) {
                logger.warn("Redis中未找到对应的密码：{}", token);
                return false;
            }
            JwtUtil.decode(token, secret);
            return true;
        } catch (Exception e) {
            logger.error("JWT 解码失败", e);
            return false;
        }
    }

    /**
     * 如果jwt过期，则返回新的jwt
     * @param token
     * @return
     */
    @Override
    public String refreshJwt(String token) {
        String secret = RedisUtil.get(token);
        if (secret == null) {
            logger.warn("未找到对应的秘钥：{}", token);
            return null;
        }
        Map<String, Claim> claims = JwtUtil.decode(token, secret);
        if(claims.get("exp").asLong()*1000 - System.currentTimeMillis()/1000 < 30 * 60 * 1000) {
           return this.generateNewJwt(claims.get("name").asString());
        }
        return token;
    }

    public String generateNewJwt(String userName) {
        String secret = UUID.randomUUID().toString();
        String newToken = JwtUtil.encode(userName, secret, expireTime);
        logger.info("generateNewJwt: secret={}", secret);
        RedisUtil.set(newToken, secret, expireTime);
        return newToken;
    }

    @Override
    public void invalidJwt(String token) {
        RedisUtil.del(token);
    }
}
