package com.bkav.edoc.web;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class OAuth2Constants {
    // Oauth response parameters and session attributes
    public static final String SCOPE = "scope";
    public static final String ERROR = "error";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String SESSION_STATE = "session_state";

    // application specific request parameters and session attributes
    public static final String CONSUMER_KEY = "consumerKey";
    public static final String CALL_BACK_URL = "callBackUrl";
    public static final String OAUTH2_GRANT_TYPE = "grantType";
    public static final String OAUTH2_AUTHZ_ENDPOINT = "authorizeEndpoint";
    public static final String OIDC_LOGOUT_ENDPOINT = "logoutEndpoint";
    public static final String OIDC_SESSION_IFRAME_ENDPOINT = "sessionIFrameEndpoint";
    public static final String NAME = "name";

    // application specific session attributes
    public static final String CODE = "code";

    // request headers
    public static final String REFERER = "referer";

    public static final String TOKEN_SSO = "TokenSSO";

    public static final String SSO_ID_TOKEN = "TokenID";

    public static final String SSO_SESSION_STATE = "session_state";

    public static final String ORGANIZATION ="Organization";

    public static final String USER_LOGIN = "userLogin";

    public static final String ORGANIZATION_INFO = "OrganizationInfo";

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String makeSlug(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

}
