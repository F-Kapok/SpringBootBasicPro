package com.fans.config;

import com.fans.common.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fans.common.CommonConstants.LOGIN_MAP;
import static com.fans.common.CommonConstants.ON_LINE_COUNT;

/**
 * @ClassName SessionListener
 * @Description: session监听器
 * @Author fan
 * @Date 2019-03-29 15:33
 * @Version 1.0
 **/
@WebListener
@Slf4j
public class SessionListener implements HttpSessionListener {
    /**
     * 计数器
     */
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.info("--> session创建");
        httpSessionEvent.getSession().setAttribute(ON_LINE_COUNT, atomicInteger.incrementAndGet());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        log.info("--> session销毁");
        HttpSession session = httpSessionEvent.getSession();
        session.setAttribute(ON_LINE_COUNT, atomicInteger.decrementAndGet());
        Object attribute = session.getAttribute(CommonConstants.CURRENT_USER);
        if (attribute != null) {
            ServletContext servletContext = session.getServletContext();
            Map<String, Object> loginMap = (Map<String, Object>) servletContext.getAttribute(CommonConstants.LOGIN_MAP);
            loginMap.remove(CommonConstants.CURRENT_USER);
            servletContext.setAttribute(LOGIN_MAP, loginMap);
            session.removeAttribute(CommonConstants.CURRENT_USER);
        }
    }
}
