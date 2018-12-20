package com.fans.common;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName RequestHolder
 * @Description: TODO 多线程存储信息类 一般object为用户类 可自定已扩展
 * @Author fan
 * @Date 2018-12-20 10:42
 * @Version 1.0
 **/
public class RequestHolder {
    private static final ThreadLocal<Object> OBJECT_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new ThreadLocal<>();

    public static void add(Object object) {
        OBJECT_HOLDER.set(object);
    }

    public static void add(HttpServletRequest request) {
        REQUEST_HOLDER.set(request);
    }

    public static Object getCurrentObject() {
        return OBJECT_HOLDER.get();
    }

    public static Object getCurrentRequest() {
        return REQUEST_HOLDER.get();
    }

    public static void remove() {
        OBJECT_HOLDER.remove();
        REQUEST_HOLDER.remove();
    }
}
