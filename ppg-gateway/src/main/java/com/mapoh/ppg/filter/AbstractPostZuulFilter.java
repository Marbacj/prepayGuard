package com.mapoh.ppg.filter;

import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @author mabohv
 * @date 2024/12/24 22:58
 */


public abstract class AbstractPostZuulFilter extends AbstractZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
}
