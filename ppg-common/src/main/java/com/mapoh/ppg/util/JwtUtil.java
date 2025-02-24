package com.mapoh.ppg.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author mabohv
 * @date 2024/12/25 15:59
 */

@Slf4j
public class JwtUtil {

    public final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    public static String encode(String name, String secret, long timeout){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //设置国旗时间，设置负载
        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + timeout))
                .withClaim("name", name)
                .sign(algorithm);
        return token;
    }

    public static Map<String, Claim> decode(String token, String secret){
        if(token == null || token.isEmpty()){
            throw new IllegalArgumentException("token is null or empty");
        }
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaims();
    }

    public  String getUsername(String token){
        return JWT.decode(token).getClaim("name").asString();
    }

    public  boolean isTokenExpired(String token){
        return JWT.decode(token).getExpiresAt().before(new Date());
    }
}
