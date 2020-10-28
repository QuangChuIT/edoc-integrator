package com.bkav.edoc.web.auth;

import com.bkav.edoc.web.OAuth2Constants;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("Pre-handle");

        String adminLogin = CookieUtil.getValue(httpServletRequest, OAuth2Constants.TOKEN_SSO);
        String organLogin = CookieUtil.getValue(httpServletRequest, OAuth2Constants.ORGANIZATION);

        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
}
