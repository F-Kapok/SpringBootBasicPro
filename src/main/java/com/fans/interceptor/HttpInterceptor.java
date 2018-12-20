package com.fans.interceptor;

import com.fans.common.RequestHolder;
import com.fans.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName HttpInterceptor
 * @Description: TODO 所有请求拦截器
 * @Author fan
 * @Date 2018-12-20 10:37
 * @Version 1.0
 **/

@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String REQUEST_TIME = "requestTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute(REQUEST_TIME, startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        removeLocalThread();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        String params = JsonMapper.obj2String(parameterMap);
        long start = (long) request.getAttribute(REQUEST_TIME);
        long end = System.currentTimeMillis();
        log.info("--> The URL of this request: {}", url);
        log.info("--> The request into the reference: {}", params);
        log.info("--> The time-consuming: {}s", (end - start));
        removeLocalThread();
    }

    private void removeLocalThread() {
        RequestHolder.remove();
    }
}
