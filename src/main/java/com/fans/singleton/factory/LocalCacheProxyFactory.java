package com.fans.singleton.factory;

import com.fans.singleton.proxy.LocalCacheProxy;

/**
 * @ClassName LocalCacheProxyFactory
 * @Description: 本地缓存工厂
 * @Author k
 * @Date 2019-08-25 04:21
 * @Version 1.0
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
