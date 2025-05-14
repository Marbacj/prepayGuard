package com.mapoh.ppg.config;

import com.mapoh.ppg.filter.JwtAuthenticationFilter;
import com.mapoh.ppg.util.JwtUtil;
import com.mapoh.ppg.util.RedisUtil;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author mabohv
 * @date 2025/2/24 13:33
 */
//
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    private JwtUtil jwtUtil;
//    private RedisUtil redisUtil;
//
//    public SecurityConfig(JwtUtil jwtUtil, RedisUtil redisUtil) {
//        this.jwtUtil = jwtUtil;
//        this.redisUtil = redisUtil;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/ppg-user/login").permitAll()
//                .and()
//                .addFilter(new JwtAuthenticationFilter(jwtUtil, redisUtil));
//    }
//}
