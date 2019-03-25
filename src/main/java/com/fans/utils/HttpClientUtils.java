package com.fans.utils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @Description: httpClient工具类
 * @Param:
 * @return:
 * @Author: fan
 * @Date: 2019/03/25 12:48
 **/
public class HttpClientUtils {
    /**
     * 建立连接的超时时间 1s
     */
    private static Integer connectTimeOut = 1000;
    /**
     * 客户端和服务进行数据交互的超时时间 10s
     */
    private static Integer socketTimeOut = 10000;

    /**
     * 请求头消息
     */
    private static String agent = "agent";
    /**
     * 单个路由连接的最大数
     */
    private static Integer maxConnPerRoute = 10;
    /**
     * 整个连接池的大小
     * 区别：比如maxConnTotal =200，maxConnPerRoute =100，
     * 那么，如果只有一个路由的话，那么最大连接数也就是100了；
     * 如果有两个路由的话，那么它们分别最大的连接数是
     * 100，总数不能超过200
     */
    private static Integer maxConnTotal = 50;

    private static final int CODE = 200;


    private static HttpClient initialize() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeOut)
                .setSocketTimeout(socketTimeOut)
                .build(); //构建requestConfig
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent(agent)
                .setMaxConnPerRoute(maxConnPerRoute)
                .setMaxConnTotal(maxConnTotal)
                //防止进程重试
                .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                .build();
    }

    public static String doGet(String url, Map<String, Object> param) {

        // 创建Httpclient对象
        CloseableHttpClient closeableHttpClient = (CloseableHttpClient) initialize();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key).toString());
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = closeableHttpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == CODE) {
                resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doPost(String url, Map<String, Object> param) {
        // 创建Httpclient对象
        CloseableHttpClient closeableHttpClient = (CloseableHttpClient) initialize();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = closeableHttpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultString;
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient closeableHttpClient = (CloseableHttpClient) initialize();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = closeableHttpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultString;
    }

    public static Integer getConnectTimeOut() {
        return connectTimeOut;
    }

    public static void setConnectTimeOut(Integer connectTimeOut) {
        HttpClientUtils.connectTimeOut = connectTimeOut;
    }

    public static Integer getSocketTimeOut() {
        return socketTimeOut;
    }

    public static void setSocketTimeOut(Integer socketTimeOut) {
        HttpClientUtils.socketTimeOut = socketTimeOut;
    }

    public static String getAgent() {
        return agent;
    }

    public static void setAgent(String agent) {
        HttpClientUtils.agent = agent;
    }

    public static Integer getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public static void setMaxConnPerRoute(Integer maxConnPerRoute) {
        HttpClientUtils.maxConnPerRoute = maxConnPerRoute;
    }

    public static Integer getMaxConnTotal() {
        return maxConnTotal;
    }

    public static void setMaxConnTotal(Integer maxConnTotal) {
        HttpClientUtils.maxConnTotal = maxConnTotal;
    }
}
