package com.fans.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * className: HttpClientProperties
 *
 * @author k
 * @version 1.0
 * @description HttpClient配置
 * @date 2018-12-20 14:14
 **/
@Component(value = "httpClientProperties")
@ConfigurationProperties(prefix = "spring.httpclient")
public class HttpClientProperties {
    /**
     * 建立连接的超时时间
     */
    private Integer connectTimeOut = 1000;
    /**
     * 客户端和服务进行数据交互的超时时间
     */
    private Integer socketTimeOut = 10000;

    /**
     * 请求头消息
     */
    private String agent = "agent";
    /**
     * 单个路由连接的最大数
     */
    private Integer maxConnPerRoute = 10;
    /**
     * 整个连接池的大小
     * 区别：比如maxConnTotal =200，maxConnPerRoute =100，
     * 那么，如果只有一个路由的话，那么最大连接数也就是100了；
     * 如果有两个路由的话，那么它们分别最大的连接数是
     * 100，总数不能超过200
     */
    private Integer maxConnTotal = 50;

    public Integer getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(Integer connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public Integer getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(Integer socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Integer getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public void setMaxConnPerRoute(Integer maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

    public Integer getMaxConnTotal() {
        return maxConnTotal;
    }

    public void setMaxConnTotal(Integer maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
    }
}
