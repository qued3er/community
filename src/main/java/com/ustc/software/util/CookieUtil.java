package com.ustc.software.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author li
 * @Title:
 * @Description:
 * @date 2020/8/2620:21
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest request,String cookiename){
        //request为空/cookiename为空
        if (request==null||cookiename==null){
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
            for (Cookie cookie :
                    cookies) {
                if (cookie.getName().equals(cookiename)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
