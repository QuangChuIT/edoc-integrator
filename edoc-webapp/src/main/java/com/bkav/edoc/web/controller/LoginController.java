package com.bkav.edoc.web.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.cache.UserCacheEntry;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.services.UserService;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.kernel.util.Base64;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.web.OAuth2Constants;
import com.bkav.edoc.web.auth.CookieUtil;
import com.bkav.edoc.web.auth.JwtService;
import com.bkav.edoc.web.auth.Oauth2Config;
import com.bkav.edoc.web.util.PropsUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthClientResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.net.ssl.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;

@Controller
public class LoginController {
    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @GetMapping(value = "/checkLogin")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException, OAuthSystemException, OAuthProblemException {
        String code = request.getParameter(OAuth2Constants.CODE);
        String session_state = request.getParameter(OAuth2Constants.SESSION_STATE);
        String redirect_url = request.getParameter("return_url");

        if (code == null || session_state == null) {
            OAuthClientRequest authRequest = Oauth2Config.buildOauthRequest(request);
            response.sendRedirect(authRequest.getLocationUri());
            return;
        }

        //String tokenSSO = CookieUtil.getValue(request, OAuth2Constants.TOKEN_SSO);

        String tokenSSO = getTokenSSO(request, response, code);

        if (tokenSSO == null) {
            LOGGER.error("Get token error for sso !!!!!!!");
            response.sendRedirect("/errors");
        } else {
            response.sendRedirect(redirect_url);
        }

    }

    @GetMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        String session_state = CookieUtil.getValue(request, OAuth2Constants.SESSION_STATE);

        String idToken = CookieUtil.getValue(request, OAuth2Constants.SSO_ID_TOKEN);
        String redirectUri = "";
        if (session != null) {
            redirectUri = (String) session.getAttribute(OAuth2Constants.CALL_BACK_URL);
        }

        if (redirectUri != null && redirectUri.equals("")) {
            redirectUri = PropsUtil.get("callBackUrl");
        }

        if (session_state != null && idToken != null) {
            StringBuilder logoutUrl = new StringBuilder();
            String OIDC_LOGOUT = PropsUtil.get("OIDC_LOGOUT_ENDPOINT");
            logoutUrl.append(OIDC_LOGOUT);
            logoutUrl.append("?id_token_hint=");
            logoutUrl.append(idToken);
            logoutUrl.append("&post_logout_redirect_uri=");
            logoutUrl.append(redirectUri);
            logoutUrl.append("&state=");
            logoutUrl.append(session_state);

            // clear cookies
            CookieUtil.clear(response, OAuth2Constants.SSO_ID_TOKEN, host_);
            CookieUtil.clear(response, OAuth2Constants.SESSION_STATE, host_);
            CookieUtil.clear(response, OAuth2Constants.TOKEN_SSO, host_);

            // clear session
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(logoutUrl.toString());
        }
    }


    private String getTokenSSO(HttpServletRequest request, HttpServletResponse response, String code) {
        HttpSession session = request.getSession(false);
        String token = null;
        try {
            String sessionState = request.getParameter(OAuth2Constants.SESSION_STATE);

            final OAuthClientRequest.TokenRequestBuilder oAuthTokenRequestBuilder =
                    new OAuthClientRequest.TokenRequestBuilder(PropsUtil.get("tokenEndpoint"));

            final OAuthClientRequest accessRequest = oAuthTokenRequestBuilder.setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(PropsUtil.get("consumerKey"))
                    .setClientSecret(PropsUtil.get("consumerSecret"))
                    .setRedirectURI((String) session.getAttribute(OAuth2Constants.CALL_BACK_URL))
                    .setCode(code)
                    .buildBodyMessage();
            try {
                SSLContext sc = SSLContext.getInstance("SSL");

                HostnameVerifier hv = new HostnameVerifier() {
                    public boolean verify(String urlHostName, SSLSession session) {
                        return true;
                    }
                };
                TrustManager[] trustAllCerts = {new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs,
                                                   String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs,
                                                   String authType) {
                    }
                }};
                sc.init(null, trustAllCerts, new SecureRandom());

                SSLContext.setDefault(sc);
                HttpsURLConnection.setDefaultHostnameVerifier(hv);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //create OAuth client that uses custom http client under the hood
            final OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            final JSONObject requestObject = requestToJson(accessRequest);
            final OAuthClientResponse oAuthResponse = oAuthClient.accessToken(accessRequest);
            final JSONObject responseObject = responseToJson(oAuthResponse);
            final String idToken = oAuthResponse.getParam("id_token");

            session.setAttribute("requestObject", requestObject);
            session.setAttribute("responseObject", responseObject);

            if (idToken != null) {
                LOGGER.info("Get id token successfully from sso " + idToken);
                DecodedJWT claims = JwtService.getClaims(idToken);
                String tokenIn = claims.getSubject();
                String organization = claims.getClaim("organization").asString();
                if (tokenIn != null && organization != null) {
                    LOGGER.info("Get subject and organization success !!!!");
                    long expiredValue = claims.getExpiresAt().getTime();
                    long startValue = claims.getIssuedAt().getTime();
                    long cookiesAgeValue = expiredValue - startValue;
                    int cookiesAge = Math.toIntExact(cookiesAgeValue);
                    // Query user from database
                    UserCacheEntry userCacheEntry = userService.findByUsername(tokenIn);
                    OrganizationCacheEntry organLogin = EdocDynamicContactServiceUtil.findByDomain(organization);
                    if (userCacheEntry != null && organLogin != null) {
                        LOGGER.info("Get user and organization success !!!!");
                        token = tokenIn;
                        // Create sso cookie with username
                        Cookie ssoCookie = CookieUtil.create(OAuth2Constants.TOKEN_SSO, token, isUseSecure, cookiesAge, host_);
                        response.addCookie(ssoCookie);
                        // update attribute for user
                        userCacheEntry.setLastLoginDate(new Date());
                        userCacheEntry.setLastLoginIP(getClientIp(request));
                        User userLogin = userService.findUserById(userCacheEntry.getUserId());
                        userLogin.setLastLoginDate(new Date());
                        userLogin.setLastLoginIP(getClientIp(request));
                        userService.updateUser(userLogin);
                        String userJson = gson.toJson(userCacheEntry);
                        String userEncodeValue = Base64.encode(userJson.getBytes(StandardCharsets.UTF_8));
                        LOGGER.info("Cookies user and organization success  1 !!!!");
                        Cookie userLoginCookies = CookieUtil.create(OAuth2Constants.USER_LOGIN, userEncodeValue, false, cookiesAge, host_);
                        response.addCookie(userLoginCookies);
                        session.setAttribute("authenticated", true);
                        // Create cookie for idToken
                        Cookie idTokenCookie = CookieUtil.create(OAuth2Constants.SSO_ID_TOKEN, idToken, isUseSecure, cookiesAge, host_);
                        // Create session state cookie
                        Cookie stateCookie = CookieUtil.create(OAuth2Constants.SSO_SESSION_STATE, sessionState, isUseSecure, cookiesAge, host_);
                        response.addCookie(stateCookie);
                        response.addCookie(idTokenCookie);
                        // Create organization cookies
                        Cookie organCookie = CookieUtil.create(OAuth2Constants.ORGANIZATION, organization, isUseSecure, cookiesAge, host_);
                        response.addCookie(organCookie);
                        session.setAttribute("organization", organization);
                        String organJson = gson.toJson(organLogin);
                        String organEncodeValue = Base64.encode(organJson.getBytes(StandardCharsets.UTF_8));
                        Cookie organLoginCookies = CookieUtil.create(OAuth2Constants.ORGANIZATION_INFO, organEncodeValue, isUseSecure, cookiesAge, host_);
                        LOGGER.info("Cookies user and organization success 2!!!!");
                        response.addCookie(organLoginCookies);
                        LOGGER.info("Cookies user and organization success 3 !!!!");
                    } else {
                        LOGGER.error("Get user and organization fail 4 !!!!");
                    }
                } else {
                    LOGGER.error("Get claims not return from sso !!!!!!!");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error when get token from sso " + e);
        }

        return token;
    }

    public static JSONObject requestToJson(final OAuthClientRequest accessRequest) {

        JSONObject obj = new JSONObject();
        obj.append("tokenEndPoint", accessRequest.getLocationUri());
        obj.append("request body", accessRequest.getBody());

        return obj;
    }

    public static JSONObject responseToJson(final OAuthClientResponse oAuthResponse) {

        JSONObject obj = new JSONObject();
        obj.append("status-code", "200");
        obj.append("id_token", oAuthResponse.getParam("id_token"));
        obj.append("access_token", oAuthResponse.getParam("access_token"));
        return obj;

    }

    private static String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    private static final boolean isUseSecure;
    private static final String host_;

    static {
        isUseSecure = GetterUtil.getBoolean(PropsUtil.get("edoc.use.secure"), false);
        host_ = GetterUtil.getString(PropsUtil.get("edoc.server.host"), "");
    }

    private final static Logger LOGGER = Logger.getLogger(LoginController.class);
}
