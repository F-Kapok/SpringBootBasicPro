package com.fans.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName CookieUtils
 * @Description: TODO Cookie工具类
 * @Author fan
 * @Date 2018/11/19 17:52
 * @Version 1.0
 **/
@Slf4j
public class CookieUtils {

    /**
     * @Description: TODO 得到Cookie的值 关闭和开启utf-8编码
     * @Param: [request, cookieName, isDecoder(false:不编码,true:utf-8)]
     * @return: java.lang.String
     * @Author: fan
     * @Date: 2018/11/19 17:53
     **/
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * @Description: TODO 得到Cookie的值 自定义编码格式
     * @Param: [request, cookieName, encodeString]
     * @return: java.lang.String
     * @Author: fan
     * @Date: 2018/11/19 17:53
     **/
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * @Description: TODO 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
     * @Param: [request, response, cookieName, cookieValue]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * @Description: TODO 设置Cookie的值 在指定时间内生效,但不编码
     * @Param: [request, response, cookieName, cookieValue, cookieMaxage]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
    }

    /**
     * @Description: TODO 设置Cookie的值 不设置生效时间,但编码
     * @Param: [request, response, cookieName, cookieValue, isEncode]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * @Description: TODO 设置Cookie的值 在指定时间内生效, 编码参数
     * @Param: [request, response, cookieName, cookieValue, cookieMaxage, isEncode]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
    }

    /**
     * @Description: TODO 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     * @Param: [request, response, cookieName, cookieValue, cookieMaxage, encodeString]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
    }

    /**
     * @Description: TODO 删除Cookie带cookie域名
     * @Param: [request, response, cookieName]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    /**
     * @Description: TODO 设置Cookie的值，并使其在指定时间内生效
     * @Param: [request, response, cookieName, cookieValue, cookieMaxage(cookie生效的最大秒数), isEncode]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:55
     **/
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                          String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            setDomainCookie(cookie, request);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: TODO 设置Cookie的值，并使其在指定时间内生效
     * @Param: [request, response, cookieName, cookieValue, cookieMaxage, encodeString]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:55
     **/
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                          String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            setDomainCookie(cookie, request);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: TODO 得到cookie的域名
     * @Param: [request]
     * @return: java.lang.String
     * @Author: fan
     * @Date: 2018/11/19 17:55
     **/
    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;

        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;
    }

    private static void setDomainCookie(Cookie cookie, HttpServletRequest request) {
        if (null != request) {
            //TODO 设置域名的cookie
            String domainName = getDomainName(request);
            log.info("--> The cookie domain name is {} ", domainName);
            if (!"localhost".equals(domainName)) {
                cookie.setDomain(domainName);
            }
        }
    }
}
