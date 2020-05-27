package com.fans.filter;

import com.fans.common.CommonConstants;
import com.fans.common.JsonData;
import com.fans.common.RequestHolder;
import com.fans.common.ResponseCode;
import com.fans.utils.JsonUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fans.common.CommonConstants.LOGIN_MAP;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * className: LoginFilter
 *
 * @author k
 * @version 1.0
 * @description 登录拦截器
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class LoginFilter implements Filter {
    private Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) {
        // 初始化拦截白名单
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
        ServletContext servletContext = session.getServletContext();
        Map<String, Object> loginMap = (Map<String, Object>) servletContext.getAttribute(LOGIN_MAP);
        if (loginMap == null || loginMap.isEmpty()) {
            String msg = "请进行登录！！！";
            returnResponse(response, msg);
            return;
        }
        //判断同一账号是否已登录
        String sid = loginMap.get(CommonConstants.CURRENT_USER).toString();
        if (!StringUtils.equals(sid, session.getId()) || user == null) {
            String msg = "会话超时，请重新登录！！！";
            returnResponse(response, msg);
            return;
        }
        RequestHolder.add(user);
        RequestHolder.add(request);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void returnResponse(HttpServletResponse response, String msg) throws IOException {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(401);
        JsonData<Object> jsonData = JsonData.failCodeMsg(ResponseCode.NEED_LOGIN.getCode(), msg);
        String obj2String = JsonUtils.obj2String(jsonData);
        response.getWriter().write(obj2String);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void destroy() {
        log.info("--> LoginFilter destroy");
    }
}
