package com.dbhys.oauth.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wangxd43 on 2017/6/20.
 */
public class CookieUtil {

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        if (null != request.getCookies()) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void removeCookie(HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void addDomainCookie(HttpServletResponse response, String domain, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        if (maxAge > 0) cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void updateDomainCookie(HttpServletRequest request, HttpServletResponse response, String domain, String name, String value, int maxAge){
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            System.out.println("--------- No cookie ---------");
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setDomain(domain);
                    cookie.setPath("/");
                    cookie.setValue(value);
                    if (maxAge > 0) cookie.setMaxAge(maxAge);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

    }
}
