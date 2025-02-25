package com.mapoh.ppg.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author mabohv
 * @date 2024/12/24 22:26
 */

@Slf4j
@Component
public class PreRequestFilter extends AbstractPreZuulFilter {


    public static Logger logger = LoggerFactory.getLogger(PreRequestFilter.class);
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 4;
    }

    @Override
    public Object cRun() {

        requestContext.set("startTime", System.currentTimeMillis());

        return null;
    }

}
