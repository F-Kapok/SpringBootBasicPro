package com.fans.singleton.proxy;

import com.fans.singleton.parent.AbstractLocalCacheProxy;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * className: LocalCacheProxy
 *
 * @author k
 * @version 1.0
 * @description 本地内存单例对象---根据需求定制（工厂生成）
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class LocalCacheProxy extends AbstractLocalCacheProxy<LocalCacheProxy> {

    /**
     * token缓存前缀
     */
    public final String TOKEN_PREFIX = "token_";

    /**
     * 并发级别默认为100，并发级别是指可以同时写缓存的线程数
     */
    private final int concurrencyLevel = 100;
    /**
     * 设置读|写缓存后60秒钟过期
     */
    private final long expireAfterAccessTime = 60;
    /**
     * 设置写缓存后60秒钟过期
     */
    private final long expireAfterWriteTime = 60;
    /**
     * 设置写后刷新缓存时间为1秒 一般使用异步刷新的方法
     */
    private final long refreshAfterWriteTime = 1;
    /**
     * 时间单位 默认秒
     */
    private final TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 缓存容器的初始容量为10
     */
    private final int initialCapacity = 10;

    /**
     * 缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
     */
    private final int maximumSize = 100;

    private final LoadingCache<String, Object> localCache = CacheBuilder.newBuilder()
            .concurrencyLevel(concurrencyLevel)
            .expireAfterAccess(expireAfterAccessTime, timeUnit)
            .expireAfterWrite(expireAfterWriteTime, timeUnit)
            .initialCapacity(initialCapacity)
            .maximumSize(maximumSize)
            //设置要统计缓存的命中率
            .recordStats()
            .removalListener(notification -> {
                removeEvent();
                switch (notification.getCause()) {
                    case SIZE:
                        log.info("--> {} : key({}) was size", this.getClass().getSimpleName(), notification.getKey());
                        break;
                    case EXPIRED:
                        log.info("--> {} : key({}) was expired", this.getClass().getSimpleName(), notification.getKey());
                        break;
                    case REPLACED:
                        log.info("--> {} : key({}) was replaced", this.getClass().getSimpleName(), notification.getKey());
                        break;
                    case COLLECTED:
                        log.info("--> {} : key({}) was collected", this.getClass().getSimpleName(), notification.getKey());
                        break;
                    case EXPLICIT:
                        log.info("--> {} : key({}) was removed(explicit)", this.getClass().getSimpleName(), notification.getKey());
                        break;
                    default:
                        break;
                }
            })
            .build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) {
                    return getValueWhenExpired("load", key);
                }

                @Override
                public ListenableFuture<Object> reload(String key, Object oldValue) {
                    return Futures.immediateFuture(getValueWhenExpired("reload", key));
                }

                @Override
                public Map<String, Object> loadAll(Iterable<? extends String> keys) throws Exception {
                    return super.loadAll(keys);
                }
            });

    /**
     * 初始化加载数据的缓存信息，读取数据库中信息或者是加载文件中的某些数据信息
     *
     * @param sign 标识
     * @param key  key
     * @return java.lang.Object
     */
    private Object getValueWhenExpired(String sign, String key) {
        switch (sign) {
            case "load":
                log.info("--> {} : load key ({})", this.getClass().getSimpleName(), key);
                break;
            case "reload":
                log.info("--> {} : reload key ({})", this.getClass().getSimpleName(), key);
                break;
            default:
                log.info("--> {} : loadAll key ({})", this.getClass().getSimpleName(), key);
                break;
        }
        Object data = getDataByKey();
        if (data != null) {
            return "object";
        } else {
            //如果没有返回值则取旧的值
            if (toMap().containsKey(key)) {
                log.warn("--> Because the database or redis or ... doesn't have key of value, So return oldValue ({})", get(key));
                return get(key);
            } else {
                log.warn("--> Because this {} doesn't have key of value, So return null", this.getClass().getSimpleName());
                return null;
            }
        }
    }

    @Override
    public String getDescription() {
        return "本地缓存";
    }

    @Override
    public Object getDataByKey() {
        return null;
    }

    @Override
    public void removeEvent() {
    }

    private LocalCacheProxy() {
    }

    public static LocalCacheProxy getInstance() {
        return Instance.INSTANCE.getLocalCache();
    }

    public LoadingCache<String, Object> getLocalCache() {
        return localCache;
    }

    private enum Instance {
        /**
         *
         */
        INSTANCE;

        private final LocalCacheProxy localCache;

        Instance() {
            this.localCache = new LocalCacheProxy();
        }

        public LocalCacheProxy getLocalCache() {
            return localCache;
        }

    }
}
