package com.fans.common;

import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName WebInitialize
 * @Description: request, response, session获取
 * @Author fan
 * @Date 2019-03-29 15:46
 * @Version 1.0
 **/
@Data
public class WebInitialize {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    public WebInitialize invoke() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        request = attributes.getRequest();
        response = attributes.getResponse();
        session = request.getSession();
        return this;
    }
}
