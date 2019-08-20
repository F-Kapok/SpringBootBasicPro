package com.fans.singleton;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LocalCacheSingleton
 * @Description: 本地内存单例对象---根据需求定制
 * @Author k
 * @Date 2019-08-20 10:28
 * @Version 1.0
 **/
@Slf4j
public class LocalCacheSingleton {

    /**
     * token缓存前缀
     */
    public final String TOKEN_PREFIX = "token_";

    /**
     * 并发级别默认为100，并发级别是指可以同时写缓存的线程数
     */
    private int concurrencyLevel = 100;
    /**
     * 设置读|写缓存后60秒钟过期
     */
    private long expireAfterAccessTime = 60;
    /**
     * 设置写缓存后60秒钟过期
     */
    private long expireAfterWriteTime = 60;
    /**
     * 设置写后刷新缓存时间为1秒 一般使用异步刷新的方法
     */
    private long refreshAfterWriteTime = 1;
    /**
     * 时间单位 默认秒
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 缓存容器的初始容量为10
     */
    private int initialCapacity = 10;

    /**
     * 缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
     */
    private int maximumSize = 100;

    private LoadingCache<String, Object> localCache = CacheBuilder.newBuilder()
            .concurrencyLevel(concurrencyLevel)
            .expireAfterAccess(expireAfterAccessTime, timeUnit)
            .expireAfterWrite(expireAfterWriteTime, timeUnit)
            .initialCapacity(initialCapacity)
            .maximumSize(maximumSize)
            //设置要统计缓存的命中率
            .recordStats()
            .removalListener(notification -> {
                //TODO 缓存移除事件添加
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
     * @param sign
     * @param key
     * @return
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
        //TODO 根据key获取数据表中的值或redis中的值 然后返回
        if (false) {
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

    private LocalCacheSingleton() {
    }

    public static LocalCacheSingleton getInstance() {
        return Instance.INSTANCE.getLocalCache();
    }

    /**
     * 获取缓存值根据指定key
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        Object value;
        try {
            value = localCache.get(key);
        } catch (Exception e) {
            log.error("--> {} get error", this.getClass().getSimpleName(), e);
            value = null;
        }
        return value;
    }

    /**
     * 设置缓存值
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        try {
            localCache.put(key, value);
            log.info("--> {} put success！", this.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("--> {} put error", this.getClass().getSimpleName(), e);
        }
    }

    /**
     * 获取所有Key的值，有一个key不存在则返回空
     *
     * @param keys
     * @return
     */
    public ImmutableMap<String, Object> getAll(Collection<String> keys) {
        try {
            return localCache.getAll(keys);
        } catch (Exception e) {
            log.error("--> {} getAll error", this.getClass().getSimpleName(), e);
            return ImmutableMap.<String, Object>builder().build();
        }
    }

    /**
     * 获取所有Key的值，有一个key不存在则忽略掉，返回其他存在的值
     *
     * @param keys
     * @return
     */
    public ImmutableMap<String, Object> getAllPresent(Collection<String> keys) {
        try {
            return localCache.getAllPresent(keys);
        } catch (Exception e) {
            log.error("--> {} getAllPresent error", this.getClass().getSimpleName(), e);
            return ImmutableMap.<String, Object>builder().build();
        }
    }

    /**
     * 获取Key的值，若不存在返回null
     *
     * @param key
     * @return
     */
    public Object getIfPresent(String key) {
        if (StringUtils.isNotBlank(key)) {
            return localCache.getIfPresent(key);
        } else {
            return null;
        }
    }

    /**
     * 删除指定key和value
     *
     * @param key
     */
    public void invalidate(String key) {
        if (StringUtils.isNotBlank(key)) {
            localCache.invalidate(key);
            log.info("--> {} invalidate success！", this.getClass().getSimpleName());
        } else {
            log.error("--> {} invalidate error : The key is empty", this.getClass().getSimpleName());
        }
    }

    /**
     * 清除所有缓存
     */
    public void invalidateAll() {
        localCache.invalidateAll();
        log.info("--> {} invalidateAll success！", this.getClass().getSimpleName());
    }

    /**
     * 批量删除指定key和value
     *
     * @param keys
     */
    public void invalidateAll(Collection<String> keys) {
        if (keys != null) {
            localCache.invalidateAll(keys);
            log.info("--> {} invalidateAll success！", this.getClass().getSimpleName());
        } else {
            log.error("--> {} invalidateAll error : Keys is null", this.getClass().getSimpleName());
        }
    }

    /**
     * 缓存刷新数据，主要针对数据表中的数据同步，如果没有新值则返回旧的值
     *
     * @param key
     */
    public void refresh(String key) {
        if (StringUtils.isNotBlank(key)) {
            Object oldValue = getIfPresent(key);
            if (oldValue != null) {
                localCache.refresh(key);
                Object newValue = get(key);
                if (newValue == null) {
                    put(key, oldValue);
                }
                log.info("--> {} refresh success！", this.getClass().getSimpleName());
            } else {
                log.warn("--> {} refresh error :  The key {} does not exist", this.getClass().getSimpleName(), key);
            }
        } else {
            log.warn("--> {} refresh error : The key is empty", this.getClass().getSimpleName());
        }
    }

    /**
     * 刷新所有缓存数据
     */
    public void refreshAll() {
        Set<String> keySet = toMap().keySet();
        keySet.forEach(this::refresh);
    }

    /**
     * 获取缓存数据转换成Map类型
     *
     * @return
     */
    public ConcurrentMap<String, Object> toMap() {
        return localCache.asMap();
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    public int getSize() {
        return toMap().size();
    }

    private enum Instance {
        /**
         *
         */
        INSTANCE;

        private LocalCacheSingleton localCache;

        Instance() {
            this.localCache = new LocalCacheSingleton();
        }

        public LocalCacheSingleton getLocalCache() {
            return localCache;
        }

    }
}
