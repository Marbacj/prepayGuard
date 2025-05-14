package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.dao.MerchantDao;
import com.mapoh.ppg.dto.LoginRequest;
import com.mapoh.ppg.dto.RegisterRequest;
import com.mapoh.ppg.entity.Merchant;
import com.mapoh.ppg.service.JwtService;

import com.mapoh.ppg.util.JwtUtil;
import com.mapoh.ppg.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author mabohv
 * @date 2024/12/30 10:43
 */


@Service
public class JwtServiceImpl implements JwtService {

    MerchantDao merchantDao;

    public static Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    public JwtServiceImpl(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    //unit: ms
    public static final int expireTime = 60 * 60 * 1000;


    @Override
    public boolean register(RegisterRequest registerRequest) {

        String name = registerRequest.getUserName();
        String idCardNumber = registerRequest.getIdCardNumber();
        String password = registerRequest.getPassword();
        String phoneNumber = registerRequest.getPhoneNumber();

        boolean result = merchantDao.existsByMerchantNameAndIdCardNumber(name, idCardNumber);

        if(result) {
            logger.info("repeat merchant name and idCardNumber: merchantName={} and idCardNumber={}", name, idCardNumber);
            return false;
        }

        merchantDao.save(new Merchant(name, password, idCardNumber, phoneNumber));
        logger.info("register success");
        return true;
    }

    //todo: logic error
    @Override
    public String login(LoginRequest loginRequest) {
        String merchantName = loginRequest.getMerchantName();
        String password = loginRequest.getPassword();

        logger.info(merchantName);
        logger.info(password);
        boolean result = merchantDao.existsByMerchantNameAndPassword(merchantName, password);
        if(!result) {
            logger.error("merchant name and password does not exist");
            return null;
        }

        logger.info("login success");
        return generateToken(merchantName);
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

    public String generateToken(String merchantName) {
        String secret = UUID.randomUUID().toString();
        String token = JwtUtil.encode(merchantName, secret, expireTime);
        logger.info("generate token : {}", token);
        RedisUtil.set(token, merchantName, expireTime);
        return token;
    }
}
