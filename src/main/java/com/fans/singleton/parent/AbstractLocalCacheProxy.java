package com.fans.singleton.parent;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * className: AbstractLocalCacheProxy
 *
 * @author k
 * @version 1.0
 * @description
 * @date 2018-12-20 14:14
 **/
@Slf4j
public abstract class AbstractLocalCacheProxy<T> {

    private Class<T> aClass;

    /**
     * description: 线程池描述
     *
     * @return java.lang.String
     * @author k
     * @date 2018-12-20 14:14
     **/
    public abstract String getDescription();

    /**
     * description: 根据key获取数据表中的值或redis中的值 然后返回
     *
     * @return java.lang.Object
     * @author k
     * @date 2018-12-20 14:14
     **/
    public abstract Object getDataByKey();

    /**
     * 缓存移除事件添加
     */
    public abstract void removeEvent();

    /**
     * description: 获取缓存值根据指定key
     *
     * @param key key
     * @return java.lang.Object
     * @author k
     * @date 2018-12-20 14:14
     **/
    public Object get(String key) {
        Object value;
        try {
            LoadingCache<String, Object> loadingCache = getLoadingCache();
            assert loadingCache != null;
            value = loadingCache.get(key);
        } catch (Exception e) {
            log.error("--> {} get error", aClass.getSimpleName(), e);
            value = null;
        }
        return value;
    }

    /**
     * description: 设置缓存值
     *
     * @param key   key
     * @param value 值
     * @author k
     * @date 2018-12-20 14:14
     **/
    public void put(String key, Object value) {
        try {
            LoadingCache<String, Object> loadingCache = getLoadingCache();
            assert loadingCache != null;
            loadingCache.put(key, value);
            log.info("--> {} put success！", aClass.getSimpleName());
        } catch (Exception e) {
            log.error("--> {} put error", aClass.getSimpleName(), e);
        }
    }

    /**
     * description: 获取所有Key的值，有一个key不存在则返回空
     *
     * @param keys key集合
     * @return com.google.common.collect.ImmutableMap<java.lang.String, java.lang.Object>
     * @author k
     * @date 2018-12-20 14:14
     **/
    public ImmutableMap<String, Object> getAll(Collection<String> keys) {
        try {
            LoadingCache<String, Object> loadingCache = getLoadingCache();
            assert loadingCache != null;
            return loadingCache.getAll(keys);
        } catch (Exception e) {
            log.error("--> {} getAll error", aClass.getSimpleName(), e);
            return ImmutableMap.<String, Object>builder().build();
        }
    }

    /**
     * 获取所有Key的值，有一个key不存在则忽略掉，返回其他存在的值
     *
     * @param keys key集合
     * @return com.google.common.collect.ImmutableMap<java.lang.String, java.lang.Object>
     */
    public ImmutableMap<String, Object> getAllPresent(Collection<String> keys) {
        try {
            LoadingCache<String, Object> loadingCache = getLoadingCache();
            assert loadingCache != null;
            return loadingCache.getAllPresent(keys);
        } catch (Exception e) {
            log.error("--> {} getAllPresent error", aClass.getSimpleName(), e);
            return ImmutableMap.<String, Object>builder().build();
        }
    }

    /**
     * 获取Key的值，若不存在返回null
     *
     * @param key key
     * @return java.lang.Object
     */
    public Object getIfPresent(String key) {
        LoadingCache<String, Object> loadingCache = getLoadingCache();
        assert loadingCache != null;
        if (StringUtils.isNotBlank(key)) {
            return loadingCache.getIfPresent(key);
        } else {
            return null;
        }
    }

    /**
     * 删除指定key和value
     *
     * @param key key
     */
    public void invalidate(String key) {
        LoadingCache<String, Object> loadingCache = getLoadingCache();
        assert loadingCache != null;
        if (StringUtils.isNotBlank(key)) {
            loadingCache.invalidate(key);
            log.info("--> {} invalidate success！", aClass.getSimpleName());
        } else {
            log.error("--> {} invalidate error : The key is empty", aClass.getSimpleName());
        }
    }

    /**
     * 清除所有缓存
     */
    public void invalidateAll() {
        LoadingCache<String, Object> loadingCache = getLoadingCache();
        assert loadingCache != null;
        loadingCache.invalidateAll();
        log.info("--> {} invalidateAll success！", aClass.getSimpleName());
    }

    /**
     * 批量删除指定key和value
     *
     * @param keys key集合
     */
    public void invalidateAll(Collection<String> keys) {
        LoadingCache<String, Object> loadingCache = getLoadingCache();
        assert loadingCache != null;
        if (keys != null) {
            loadingCache.invalidateAll(keys);
            log.info("--> {} invalidateAll success！", aClass.getSimpleName());
        } else {
            log.error("--> {} invalidateAll error : Keys is null", aClass.getSimpleName());
        }
    }

    /**
     * 缓存刷新数据，主要针对数据表中的数据同步，如果没有新值则返回旧的值
     *
     * @param key key
     */
    public void refresh(String key) {
        LoadingCache<String, Object> loadingCache = getLoadingCache();
        assert loadingCache != null;
        if (StringUtils.isNotBlank(key)) {
            Object oldValue = getIfPresent(key);
            if (oldValue != null) {
                loadingCache.refresh(key);
                Object newValue = get(key);
                if (newValue == null) {
                    put(key, oldValue);
                }
                log.info("--> {} refresh success！", aClass.getSimpleName());
            } else {
                log.warn("--> {} refresh error :  The key {} does not exist", aClass.getSimpleName(), key);
            }
        } else {
            log.warn("--> {} refresh error : The key is empty", aClass.getSimpleName());
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
     * @return java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object>
     */
    public ConcurrentMap<String, Object> toMap() {
        LoadingCache<String, Object> loadingCache = getLoadingCache();
        assert loadingCache != null;
        return loadingCache.asMap();
    }

    /**
     * 获取缓存大小
     *
     * @return int
     */
    public int getSize() {
        return toMap().size();
    }

    /**
     * 反射获取泛型类型
     *
     * @return com.google.common.cache.LoadingCache<java.lang.String, java.lang.Object>
     */
    private LoadingCache<String, Object> getLoadingCache() {
        try {
            //获取泛型类型数组
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            //获取泛型类型
            aClass = (Class<T>) pt.getActualTypeArguments()[0];
            Method instance = aClass.getDeclaredMethod("getInstance");
            Method method = aClass.getMethod("getLocalCache");
            return (LoadingCache<String, Object>) method.invoke(instance.invoke(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
