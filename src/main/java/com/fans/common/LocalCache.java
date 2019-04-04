package com.fans.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LocalCache
 * @Description: token的本地缓存
 * @Author fan
 * @Date 2018-11-23 14:14
 * @Version 1.0
 **/
@Slf4j
public class LocalCache {

    public static final String TOKEN_PREFIX = "token_";
    public static final String NULL = "null";

    private static LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) {
                    //在这里可以初始化加载数据的缓存信息，读取数据库中信息或者是加载文件中的某些数据信息
                    return "null";
                }
            });

    public static String getLoadingCache(String key) {
        String token;
        try {
            token = loadingCache.get(key);
            if (NULL.equals(token)) {
                return null;
            }
        } catch (ExecutionException e) {
            log.error("LocalCache get error", e);
            return null;
        }
        return token;
    }

    public static void setLoadingCache(String key, String value) {
        loadingCache.put(key, value);
    }
}
