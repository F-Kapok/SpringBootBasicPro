package com.fans.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * @ClassName DruidConfiguration
 * @Description: TODO druid数据源配置
 * @Author fan
 * @Date 2018-12-20 11:40
 * @Version 1.0
 **/
@Configuration
@Slf4j
public class DruidConfiguration {
    private static final String DB_PREFIX = "spring.datasource";

    /**
     * @Description: TODO 初始化默认dataSource
     * @Param: []
     * @return: javax.sql.DataSource
     * @Author: fan
     * @Date: 2018/12/20 12:31
     **/
    @Bean
    @ConfigurationProperties(prefix = DB_PREFIX)
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * @Description: TODO 配置druidServlet相关内容
     * @Param: []
     * @return: org.springframework.boot.web.servlet.ServletRegistrationBean
     * @Author: fan
     * @Date: 2018/12/20 12:31
     **/
    @Bean
    public ServletRegistrationBean druidServlet() {
        log.info("init Druid Servlet Configuration");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //IP白名单
        servletRegistrationBean.addInitParameter("allow", "*");
        //IP黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "druid");
        servletRegistrationBean.addInitParameter("loginPassword", "druid");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    /**
     * @Description: TODO 配置druid过滤内容
     * @Param: []
     * @return: org.springframework.boot.web.servlet.FilterRegistrationBean
     * @Author: fan
     * @Date: 2018/12/20 12:31
     **/
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
