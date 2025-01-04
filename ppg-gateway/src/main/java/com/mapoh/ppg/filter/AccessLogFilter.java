package com.mapoh.ppg.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mabohv
 * @date 2024/12/24 17:23
 */

@Slf4j
@Component
public class AccessLogFilter extends AbstractPostZuulFilter {


    public static Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public Object cRun() {

        HttpServletRequest request = requestContext.getRequest();
        Long startTime = System.currentTimeMillis();
        String uri = request.getRequestURI();
        long duration = System.currentTimeMillis() - startTime;

        logger.info("uri " + uri + " duration " + duration / 100 + " ms");
        return null;
    }
}

