package com.mapoh.ppg.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import java.io.IOException;

/**
 * @author mabohv
 * @date 2024/12/24 22:48
 */

public abstract class AbstractZuulFilter extends ZuulFilter {

    RequestContext requestContext;

    private final static String NEXT = "next";

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean) ctx.getOrDefault(NEXT, true);
    }

    @Override
    public Object run() throws ZuulException {

        requestContext = RequestContext.getCurrentContext();
        return cRun();
    }
    protected abstract Object cRun();

    Object fail(int code, String msg) {

        requestContext.set(NEXT, false);
        requestContext.setSendZuulResponse(false);
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");
        requestContext.setResponseStatusCode(code);
        requestContext.setResponseBody(String.format("{\"result\": \"%s!\"}", msg));

        return null;
    }

    Object success(){
        requestContext.set(NEXT, true);
        return null;
    }
}
