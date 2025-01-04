package com.mapoh.ppg.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import rx.annotations.Beta;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mabohv
 * @date 2024/12/24 23:13
 */

@Slf4j
@Component
@SuppressWarnings("all")
public class RateLimiterFilter extends AbstractPreZuulFilter{

    private static Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);

    @Beta
    RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Override
    protected Object cRun() {

        HttpServletRequest context = requestContext.getRequest();

        if(rateLimiter.tryAcquire()) {
            logger.info("get rate token success!");
            return success();
        } else {
            logger.error("get rate token fail!");
            return fail(402, "get rate token fail!");
        }
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 2;
    }
}
