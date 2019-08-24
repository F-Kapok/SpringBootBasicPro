package com.fans.singleton.factory;

import com.fans.singleton.proxy.ThreadPoolProxy;

/**
 * @ClassName ThreadPoolProxyFactory
 * @Description: 线程池工厂
 * @Author k
 * @Date 2019-08-25 04:07
 * @Version 1.0
 **/
public class ThreadPoolProxyFactory {

    /**
     * 默认单例线程池 volatile防止重排序带来的线程安全问题
     */
    private static volatile ThreadPoolProxy threadPoolProxy;


    public static ThreadPoolProxy getThreadPoolProxy() {
        if (threadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (threadPoolProxy == null) {
                    threadPoolProxy = ThreadPoolProxy.getInstance();
                }
            }
        }
        return threadPoolProxy;
    }

    /**
     * 根据需求增加不同的单例线程池
     */
    private static Object getThreadPoolProxyGoOn() {
        return new Object();
    }
}
