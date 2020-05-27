package com.fans.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * className: CookieUtils
 *
 * @author k
 * @version 1.0
 * @description Cookie工具类
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class CookieUtils {

    /**
     * description: 得到Cookie的值 关闭和开启utf-8编码
     *
     * @param request    请求
     * @param cookieName cookie名
     * @param isDecoder  是否编码 (false:不编码,true:utf-8)
     * @return java.lang.String
     * @author k
     * @date 2018/11/19 17:53
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
     * description: 得到Cookie的值 自定义编码格式
     *
     * @param request      请求
     * @param cookieName   cookie名
     * @param encodeString 编码str
     * @return java.lang.String
     * @author k
     * @date 2018/11/19 17:53
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
     * description: 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
     *
     * @param request     请求
     * @param response    响应
     * @param cookieName  cookie名
     * @param cookieValue cookie值
     * @author k
     * @date 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * @Description:
     * @Param: [request, response, cookieName, cookieValue, cookieMaxAge]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/19 17:54
     **/
    /**
     * description: 设置Cookie的值 在指定时间内生效,但不编码
     *
     * @param request      请求
     * @param response     响应
     * @param cookieName   cookie名
     * @param cookieValue  cookie值
     * @param cookieMaxAge cookie周期
     * @author k
     * @date 2018/11/19 17:54
     **/
    private static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                  String cookieValue, int cookieMaxAge) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxAge, false);
    }

    /**
     * description: 设置Cookie的值 不设置生效时间,但编码
     *
     * @param request     请求
     * @param response    响应
     * @param cookieName  cookie名
     * @param cookieValue cookie值
     * @param isEncode    是否编码
     * @author k
     * @date 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * description: 设置Cookie的值 在指定时间内生效, 编码参数
     *
     * @param request      请求
     * @param response     响应
     * @param cookieName   cookie名
     * @param cookieValue  cookie值
     * @param cookieMaxAge cookie周期
     * @param isEncode     是否编码
     * @author k
     * @date 2018/11/19 17:54
     **/
    private static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                  String cookieValue, int cookieMaxAge, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, isEncode);
    }

    /**
     * description: 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     *
     * @param request      请求
     * @param response     响应
     * @param cookieName   cookie名
     * @param cookieValue  cookie值
     * @param cookieMaxAge cookie周期
     * @param encodeString 编码str
     * @author k
     * @date 2018/11/19 17:54
     **/
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, encodeString);
    }

    /**
     * description: 删除Cookie带cookie域名
     *
     * @param request    请求
     * @param response   响应
     * @param cookieName cookie名
     * @author k
     * @date 2018/11/19 17:54
     **/
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    /**
     * description: 设置Cookie的值，并使其在指定时间内生效
     *
     * @param request      请求
     * @param response     响应
     * @param cookieName   cookie名
     * @param cookieValue  cookie值
     * @param cookieMaxAge cookie周期
     * @param isEncode     是否编码
     * @author k
     * @date 2018/11/19 17:55
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
     * description: 设置Cookie的值，并使其在指定时间内生效
     *
     * @param request      请求
     * @param response     响应
     * @param cookieName   cookie名
     * @param cookieValue  cookie值
     * @param cookieMaxAge cookie周期
     * @param encodeString 编码Str
     * @author k
     * @date 2018/11/19 17:55
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
     *
     *
     * @param request
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxAge
     * @return
     */
    /**
     * description: 设置cookie
     *
     * @param request      请求
     * @param cookieName   cookie名
     * @param cookieValue  cookie值
     * @param cookieMaxAge cookie周期
     * @return javax.servlet.http.Cookie
     * @author k
     * @date 2018/11/19 17:55
     **/
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
     * description: 得到cookie的域名
     *
     * @param request 请求
     * @return java.lang.String
     * @author k
     * @date 2018/11/19 17:55
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
            String str = ":";
            if (serverName.indexOf(str) > 0) {
                String[] ary = serverName.split(str);
                serverName = ary[0];
            }
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            int size = 3;
            if (isIp(serverName)) {
                domainName = serverName;
            } else if (len > size) {
                // www.xxx.com.cn
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
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

    /**
     * description: 去掉IP字符串前后所有的空格
     *
     * @param ip ip地址
     * @return java.lang.String
     * @author k
     * @date 2019/11/22 19:15
     **/
    private static String trimSpaces(String ip) {
        String separator = " ";
        while (ip.startsWith(separator)) {
            ip = ip.substring(1).trim();
        }
        while (ip.endsWith(separator)) {
            ip = ip.substring(0, ip.length() - 1).trim();
        }
        return ip;
    }

    /**
     * description: 判断是否是一个IP
     *
     * @param ip ip地址
     * @return boolean
     * @author k
     * @date 2019/11/22 19:15
     **/
    public static boolean isIp(String ip) {
        boolean b = false;
        ip = trimSpaces(ip);
        if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String[] s = ip.split("\\.");
            if (Integer.parseInt(s[0]) < 255) {
                if (Integer.parseInt(s[1]) < 255) {
                    if (Integer.parseInt(s[2]) < 255) {
                        if (Integer.parseInt(s[3]) < 255) {
                            b = true;
                        }
                    }
                }
            }
        }
        return b;
    }
}
