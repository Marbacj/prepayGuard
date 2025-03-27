package com.mapoh.ppg.filter;

import com.mapoh.ppg.util.JwtUtil;
import com.mapoh.ppg.util.RedisUtil;
import com.mapoh.ppg.vo.CommonResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mabohv
 * @date 2025/2/24 10:56
 */

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private RedisUtil redisUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisUtil redisUtil) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        String token = httpServletRequest.getHeader("Authorization");
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//            if(!jwtUtil.isTokenExpired(token) && !redisUtil.isTokenBlacklisted(token)){
//                String userName = jwtUtil.getUsername(token);
//                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userName, null, null));
//            }
//        }
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
