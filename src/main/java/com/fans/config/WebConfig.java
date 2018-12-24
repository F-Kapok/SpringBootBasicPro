package com.fans.config;

import com.fans.filter.LoginFilter;
import com.fans.interceptor.HttpInterceptor;
import com.google.common.collect.Lists;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @ClassName WebConfig
 * @Description: TODO 类似于web.xml配置
 * @Author fan
 * @Date 2018-12-20 10:52
 * @Version 1.0
 **/
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
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
        filterRegistrationBean.setUrlPatterns(urls);
        filterRegistrationBean.addInitParameter("exclusions", "/login.do,/swagger-ui.html");
        return filterRegistrationBean;
    }
}
