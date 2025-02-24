package com.mapoh.ppg.filter;

import com.mapoh.ppg.feign.UserClientFeign;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebFault;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Token 登录拦截器
 *
 * @author mabohv
 * @date 2024/12/26 16:40
 */
@Component
@WebFilter
public class LoginFilter extends AbstractPreZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_REDIRECT = 302;
    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";

    @Value("${custom.server-hosts.ppg-user}")
    private String serverHost;

    private final UserClientFeign userClientFeign;

    /**
     * 配置路径白名单
     */
    private static final Set<String> WHITELIST_PATHS = new HashSet<>(
            Arrays.asList("/register", "/")
    );

    public LoginFilter(UserClientFeign userClientFeign) {
        this.userClientFeign = userClientFeign;
    }


    @Override
    protected Object cRun() {

        HttpServletRequest request = requestContext.getRequest();
        String requestPath = request.getRequestURI();

        for(String str : WHITELIST_PATHS) {
            if(requestPath.contains(str)) {
                logger.info("Request path '{}' is whitelisted, bypassing token validation", requestPath);
                return success();
            }
        }
        // 检查是否在白名单路径中
//        if (isWhitelisted(requestPath)) {
//
//        }

        // 检查 token
        String token = request.getParameter("token");
        if (isTokenInvalid(token)) {
            logger.error("Token is empty or invalid");
            blockRequest(requestContext, HTTP_UNAUTHORIZED, "Token is empty or invalid");
            return null;
        }

        try {
            boolean isValidToken = validateToken(token);
            if (!isValidToken) {
                handleInvalidToken(request);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error during token validation", e);
            blockRequest(requestContext, HTTP_UNAUTHORIZED, "Token validation error");
            return null;
        }

        return success();
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 3;
    }

    private boolean isWhitelisted(String path) {
        return WHITELIST_PATHS.contains(path);
    }

    private boolean isTokenInvalid(String token) {
        return token == null || token.trim().isEmpty();
    }

    private boolean validateToken(String token) throws Exception {
        return userClientFeign.checkJwt(token).getData();
    }

    private void handleInvalidToken(HttpServletRequest request) {
        String redirectUrl = serverHost + "/login?redirect=" + request.getRequestURL();
        logger.warn("Invalid token, redirecting to login: {}", redirectUrl);
        redirectToLogin(requestContext, redirectUrl);
    }

    private void blockRequest(RequestContext context, int statusCode, String message) {
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(statusCode);
        context.setResponseBody(message);
        context.getResponse().setContentType(JSON_CONTENT_TYPE);
    }

    private void redirectToLogin(RequestContext context, String redirectUrl) {
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(HTTP_REDIRECT);
        context.getResponse().setHeader("Location", redirectUrl);
    }
}