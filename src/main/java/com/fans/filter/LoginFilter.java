package com.fans.filter;

import com.fans.common.CommonConstants;
import com.fans.common.RequestHolder;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @ClassName LoginFilter
 * @Description: TODO 登录拦截器
 * @Author fan
 * @Date 2018-12-20 13:38
 * @Version 1.0
 **/
@Slf4j
public class LoginFilter implements Filter {
    private Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) {
        //TODO 初始化拦截白名单
        String exclusionUrl = filterConfig.getInitParameter("exclusions");
        List<String> exclusionList = Splitter.on(",").omitEmptyStrings().splitToList(exclusionUrl);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionList);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        if (exclusionUrlSet.contains(servletPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpSession session = request.getSession();
        Object user = session.getAttribute(CommonConstants.CURRENT_USER);
        if (user == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("[{\"msg\":\"未登录请进行登录操作\",\"status\":\"1\"}]");
            return;
        }
        RequestHolder.add(user);
        RequestHolder.add(request);
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {
        log.info("--> LoginFilter destroy");
    }
}
