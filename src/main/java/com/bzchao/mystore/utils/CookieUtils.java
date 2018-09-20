package com.bzchao.mystore.utils;

import javax.servlet.http.Cookie;

/**
 * cookie工具类
 */
public class CookieUtils {
    public static Cookie getCookie(String name, Cookie[] cookies) {
        if (cookies == null || cookies.length < 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }
}
