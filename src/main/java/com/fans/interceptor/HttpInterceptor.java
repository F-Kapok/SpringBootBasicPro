package com.fans.interceptor;

import com.fans.common.RequestHolder;
import com.fans.utils.JsonUtils;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * className: HttpInterceptor
 *
 * @author k
 * @version 1.0
 * @description 所有请求拦截器
 * @date 2018-12-20 14:14
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
        String params;
        Map parameterMap = request.getParameterMap();
        if (parameterMap.size() != 0) {
            params = JsonUtils.obj2String(parameterMap);
        } else {
            params = IOUtils.toString(request.getInputStream(), Charsets.UTF_8.name());
        }
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
