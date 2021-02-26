package com.bkav.edoc.web.auth;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void create(HttpServletResponse httpServletResponse, String name,
                              String value, Boolean secure, Integer maxAge, String domain) {
        Cookie cookie = create(name, value, secure, maxAge, domain);
        httpServletResponse.addCookie(cookie);
    }

    public static Cookie create(String name,
                                String value, Boolean secure, Integer maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        if (!domain.equals("")) {
            cookie.setDomain(domain);
        }
        cookie.setPath("/");
        return cookie;
    }

    public static void clear(HttpServletResponse httpServletResponse, String name, String domain) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        if (!domain.equals("")) {
            cookie.setDomain(domain);
        }
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }

    public static void clearAllCookies(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String domain) {
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setPath("/");
            if (!domain.equals("")) {
                cookie.setDomain(domain);
            }
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            httpServletResponse.addCookie(cookie);
        }
    }

    public static String getValue(HttpServletRequest httpServletRequest, String name) {
        Cookie cookie = WebUtils.getCookie(httpServletRequest, name);
        return cookie != null ? cookie.getValue() : null;
    }
}
