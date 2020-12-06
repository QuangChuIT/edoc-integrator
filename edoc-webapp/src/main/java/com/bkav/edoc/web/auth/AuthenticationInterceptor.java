package com.bkav.edoc.web.auth;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.web.OAuth2Constants;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info("Re Handler Request URL::" + httpServletRequest.getRequestURL().toString()
                + ":: Start Time=" + System.currentTimeMillis());
        httpServletRequest.setAttribute("startTime", startTime);
        String userLogin = CookieUtil.getValue(httpServletRequest, OAuth2Constants.TOKEN_SSO);
        String organLogin = CookieUtil.getValue(httpServletRequest, OAuth2Constants.ORGANIZATION);
        String userLoginInfo = CookieUtil.getValue(httpServletRequest, OAuth2Constants.USER_LOGIN);
        String organizationInfo = CookieUtil.getValue(httpServletRequest, OAuth2Constants.ORGANIZATION_INFO);
        String host = GetterUtil.getString(PropsUtil.get("edoc.server.host"), "");
        if (userLogin != null && organLogin != null) {
            if (userLoginInfo == null || organizationInfo == null) {
                CookieUtil.clear(httpServletResponse, OAuth2Constants.TOKEN_SSO, host);
                CookieUtil.clear(httpServletResponse, OAuth2Constants.ORGANIZATION, host);
                try {
                    OAuthClientRequest authClientRequest = Oauth2Config.buildOauthRequest(httpServletRequest);
                    logger.warn("Redirect url to sso: " + authClientRequest.getLocationUri());
                    httpServletResponse.sendRedirect(authClientRequest.getLocationUri());
                    return false;
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return false;
                }
            }
        }
        if (userLogin == null || organLogin == null) {
            try {
                CookieUtil.clear(httpServletResponse, OAuth2Constants.ORGANIZATION, host);
                CookieUtil.clear(httpServletResponse, OAuth2Constants.TOKEN_SSO, host);
                OAuthClientRequest authClientRequest = Oauth2Config.buildOauthRequest(httpServletRequest);
                logger.warn("Redirect url to sso: " + authClientRequest.getLocationUri());
                httpServletResponse.sendRedirect(authClientRequest.getLocationUri());
                return false;
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.info(" Post handler Request URL::" + httpServletRequest.getRequestURL().toString()
                + " Sent to Handler :: Current Time=" + System.currentTimeMillis());

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        long startTime = (Long) httpServletRequest.getAttribute("startTime");
        logger.info("After completion Request URL::" + httpServletRequest.getRequestURL().toString()
                + ":: End Time=" + System.currentTimeMillis());
        logger.info("After completion Request URL::" + httpServletRequest.getRequestURL().toString()
                + ":: Time Taken=" + (System.currentTimeMillis() - startTime));
    }

    private static final Logger logger = Logger.getLogger(AuthenticationInterceptor.class);
}
