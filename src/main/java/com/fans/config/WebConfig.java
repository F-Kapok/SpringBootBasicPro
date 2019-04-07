package com.fans.config;

import com.fans.filter.LoginFilter;
import com.fans.filter.RequestBodyFilter;
import com.fans.interceptor.HttpInterceptor;
import com.google.common.collect.Lists;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @ClassName WebConfig
 * @Description: 类似于web.xml配置
 * @Author fan
 * @Date 2018-12-20 10:52
 * @Version 1.0
 **/
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //不拦截的列表
        List<String> exclude = Lists.newArrayList();
        exclude.add("/static/**");
        registry.addInterceptor(new HttpInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(exclude);
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/");
        /*放行swagger*/
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
        LoginFilter loginFilter = new LoginFilter();
        filterRegistrationBean.setFilter(loginFilter);
        //设置拦截请求路径
        List<String> urls = Lists.newArrayList();
        //所有路径都拦截
        urls.add("*.do");
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setUrlPatterns(urls);
        filterRegistrationBean.addInitParameter("exclusions", "/login.do,/swagger-ui.html");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean RequestBodyFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        RequestBodyFilter requestBodyFilter = new RequestBodyFilter();
        filterRegistrationBean.setFilter(requestBodyFilter);
        List<String> urls = Lists.newArrayList();
        urls.add("*.do");
        filterRegistrationBean.setEnabled(true);
        //order的数值越小，优先级越高
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        filterRegistrationBean.setUrlPatterns(urls);
        return filterRegistrationBean;
    }

    /**
     * @Description: 跨域调用开放
     * @Param: []
     * @return: org.springframework.boot.web.servlet.FilterRegistrationBean<org.springframework.web.filter.CorsFilter>
     * @Author: fan
     * @Date: 2019/03/22 12:48
     **/
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        // 1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 放行哪些原始域
        config.addAllowedOrigin("*");
        // 是否发送Cookie信息
        config.setAllowCredentials(true);
        // 放行哪些原始域(请求方式)
        config.addAllowedMethod("*");
        // 放行哪些原始域(头部信息)
        config.addAllowedHeader("*");
        // 2.添加映射路径
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        // 3.返回新的CorsFilter
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(configSource));
        bean.setOrder(0);
        return bean;
    }
}
