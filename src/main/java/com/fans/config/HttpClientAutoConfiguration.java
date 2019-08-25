package com.fans.config;

import com.fans.common.HttpClientProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @ClassName HttpClientAutoConfiguration
 * @Description: HttpClient自动注入
 * @Author fan
 * @Date 2019-03-05 17:42
 * @Version 1.0
 **/
@Configuration
@ConditionalOnClass({HttpClient.class})
public class HttpClientAutoConfiguration {

    @Resource(name = "httpClientProperties")
    private HttpClientProperties httpClientProperties;


    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(httpClientProperties.getConnectTimeOut())
                .setSocketTimeout(httpClientProperties.getSocketTimeOut())
                .build(); //构建requestConfig
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent(httpClientProperties.getAgent())
                .setMaxConnPerRoute(httpClientProperties.getMaxConnPerRoute())
                .setMaxConnTotal(httpClientProperties.getMaxConnTotal())
                //防止进程重试
                .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                .build();
    }
}
