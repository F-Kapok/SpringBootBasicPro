package com.fans.singleton.factory;

import com.fans.singleton.proxy.LocalCacheProxy;

/**
 * className: LocalCacheProxyFactory
 *
 * @author k
 * @version 1.0
 * @description 本地缓存工厂
 * @date 2018-12-20 14:14
 **/
public class LocalCacheProxyFactory {

    private static volatile LocalCacheProxy localCacheProxy;

    public static LocalCacheProxy getLocalCacheProxy() {
        if (localCacheProxy == null) {
            synchronized (LocalCacheProxyFactory.class) {
                if (localCacheProxy == null) {
                    localCacheProxy = LocalCacheProxy.getInstance();
                }
            }
        }
        return localCacheProxy;
    }

    /**
     * 根据需求增加不同的单例缓存
     */
    private static Object getLocalCacheProxyGoOn() {
        return new Object();
    }
}
