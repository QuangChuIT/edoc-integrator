package com.bkav.edoc.web.auth;

import com.bkav.edoc.web.OAuth2Constants;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Oauth2Config {
    public static OAuthClientRequest buildOauthRequest(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            session = request.getSession(true);
        }
        String consumerKey = PropsUtil.get("consumerKey");
        String authzEndpoint = PropsUtil.get("authzEndpoint");
        String authzGrantType = PropsUtil.get("authzGrantType");
        String scope = PropsUtil.get("scope");
        String callBackUrl = PropsUtil.get("callBackUrl");

        session.setAttribute(OAuth2Constants.OAUTH2_GRANT_TYPE, authzGrantType);
        session.setAttribute(OAuth2Constants.CONSUMER_KEY, consumerKey);
        session.setAttribute(OAuth2Constants.SCOPE, scope);
        session.setAttribute(OAuth2Constants.CALL_BACK_URL, callBackUrl);
        session.setAttribute(OAuth2Constants.OAUTH2_AUTHZ_ENDPOINT, authzEndpoint);

        OAuthClientRequest.AuthenticationRequestBuilder oAuthAuthenticationRequestBuilder =
                new OAuthClientRequest.AuthenticationRequestBuilder(authzEndpoint);

        oAuthAuthenticationRequestBuilder
                .setClientId(consumerKey)
                .setRedirectURI(callBackUrl)
                .setScope(scope)
                .setResponseType(authzGrantType);

        // Build the new response mode with form post.
        OAuthClientRequest authzRequest = null;
        try {
            authzRequest = oAuthAuthenticationRequestBuilder.buildQueryMessage();
        } catch (OAuthSystemException e) {
            logger.error(e.getMessage());
        }
        return authzRequest;
    }

    private static final Logger logger = Logger.getLogger(AuthenticationInterceptor.class);
}
