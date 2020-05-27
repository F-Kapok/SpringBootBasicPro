package com.fans.common;

import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * className: WebInitialize
 *
 * @author k
 * @version 1.0
 * @description request, response, session获取
 * @date 2018-12-20 14:14
 **/
@Data
public class WebInitialize {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    public WebInitialize() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        request = attributes.getRequest();
        response = attributes.getResponse();
        session = request.getSession();
    }

    /**
     * 获取域名
     *
     * @return domain
     */
    public String getDomain() {
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    /**
     * 获取请求来源
     *
     * @return origin
     */
    public String getOrigin() {
        return request.getHeader("Origin");
    }
}
