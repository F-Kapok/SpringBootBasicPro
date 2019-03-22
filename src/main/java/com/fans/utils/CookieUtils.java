package com.fans.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName CookieUtils
 * @Description: Cookie工具类
 * @Author fan
 * @Date 2018/11/19 17:52
 * @Version 1.0
 **/
@Slf4j
public class CookieUtils {

    /**
     * @Description: 得到Cookie的值 关闭和开启utf-8编码
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
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                    } else {
                        retValue = cookie.getValue();
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
     * @Description: 得到Cookie的值 自定义编码格式
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
     * @Description: 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
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
     * @Description: 设置Cookie的值 在指定时间内生效,但不编码
     * @Param: [request, response, cookieName, cookieValue, cookieMaxAge]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    private static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                  String cookieValue, int cookieMaxAge) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxAge, false);
    }

    /**
     * @Description: 设置Cookie的值 不设置生效时间,但编码
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
     * @Description: 设置Cookie的值 在指定时间内生效, 编码参数
     * @Param: [request, response, cookieName, cookieValue, cookieMaxAge, isEncode]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    private static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                  String cookieValue, int cookieMaxAge, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, isEncode);
    }

    /**
     * @Description: 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     * @Param: [request, response, cookieName, cookieValue, cookieMaxAge, encodeString]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, encodeString);
    }

    /**
     * @Description: 删除Cookie带cookie域名
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
     * @Description: 设置Cookie的值，并使其在指定时间内生效
     * @Param: [request, response, cookieName, cookieValue, cookieMaxAge(cookie生效的最大秒数), isEncode]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:55
     **/
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxAge, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8.name());
            }
            Cookie cookie = getCookie(request, cookieName, cookieValue, cookieMaxAge);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 设置Cookie的值，并使其在指定时间内生效
     * @Param: [request, response, cookieName, cookieValue, cookieMaxAge, encodeString]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:55
     **/
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxAge, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = getCookie(request, cookieName, cookieValue, cookieMaxAge);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置cookie
     *
     * @param request
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxAge
     * @return
     */
    private static Cookie getCookie(HttpServletRequest request, String cookieName, String cookieValue, int cookieMaxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (cookieMaxAge > 0) {
            cookie.setMaxAge(cookieMaxAge);
        }
        setDomainCookie(cookie, request);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * @Description: 得到cookie的域名
     * @Param: [request]
     * @return: java.lang.String
     * @Author: fan
     * @Date: 2018/11/19 17:55
     **/
    private static String getDomainName(HttpServletRequest request) {
        String domainName;

        String serverName = request.getRequestURL().toString();
        if (StringUtils.isBlank(serverName)) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            int size = 3;
            if (len > size) {
                // www.xxx.com.cn
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }
        String str = ":";
        if (domainName.indexOf(str) > 0) {
            String[] ary = domainName.split(str);
            domainName = ary[0];
        }
        return domainName;
    }

    private static void setDomainCookie(Cookie cookie, HttpServletRequest request) {
        if (null != request) {
            // 设置域名的cookie
            String domainName = getDomainName(request);
            log.info("--> The cookie domain name is {} ", domainName);
            String localhost = "localhost";
            if (!localhost.equals(domainName)) {
                cookie.setDomain(domainName);
            }
        }
    }
}
