package com.fans.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName
 * @Description:
 * @Author k
 * @Date 2019-09-10 09:53
 * @Version 1.0
 **/

/**
 * className: LocalCache
 *
 * @author k
 * @version 1.0
 * @description 自定义可过期缓存
 * @date 2018-12-20 14:14
 **/
public class CustomLocalCache {
    /**
     * 默认缓存时长 单位s
     */
    private static final int DEFAULT_TIMEOUT = 3600;
    /**
     * 默认缓存容量
     */
    private static final int DEFAULT_SIZE = 1000;

    /**
     * 存储数据
     */
    private static final ConcurrentHashMap<String, Object> CACHE_DATA;

    /**
     * 定时器  用来控制 缓存的超时时间
     */
    private static final ScheduledExecutorService executorService;

    //初始化
    static {
        CACHE_DATA = new ConcurrentHashMap<>(DEFAULT_SIZE);
        executorService = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder()
                .setNameFormat("scheduledLocalCache-task-runner-%d")
                .build());
    }

    /**
     * 私有化构造函数
     */
    private CustomLocalCache() {
    }

    /**
     * 增加缓存 默认有效时长
     *
     * @param key   key
     * @param value 值
     */
    public static void put(String key, Object value) {
        CACHE_DATA.put(key, value);
        //定时器 调度任务，用于根据 时间 定时清除 对应key 缓存
        executorService.schedule(new TimerTask() {
            @Override
            public void run() {
                remove(key);
            }
        }, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 增加缓存  并设置缓存时长 单位 s
     *
     * @param key     key
     * @param value   值
     * @param timeout 缓存时长 单位s
     */
    public static void put(String key, Object value, int timeout) {
        CACHE_DATA.put(key, value);
        //lambda 替换匿名内部类
        executorService.schedule(() -> remove(key), timeout, TimeUnit.SECONDS);
    }

    /**
     * 增加缓存 并指定过期时间点
     *
     * @param key        key
     * @param value      值
     * @param expireTime 指定过期时间点
     */
    public static void put(String key, Object value, LocalDateTime expireTime) {
        CACHE_DATA.put(key, value);
        LocalDateTime nowTime = LocalDateTime.now();
        if (nowTime.isAfter(expireTime)) {
            //TODO 时间设置异常 待处理
        }
        long seconds = Duration.between(nowTime, expireTime).getSeconds();
        executorService.schedule(() -> remove(key), seconds, TimeUnit.SECONDS);
    }

    /**
     * 批量增加缓存
     *
     * @param cache 缓存信息
     */
    public static void put(Map<String, Object> cache) {
        if (!CollectionUtils.isEmpty(cache)) {
            cache.forEach(CustomLocalCache::put);
        }
    }

    public static void put(Map<String, Object> cache, int timeout) {
        if (!CollectionUtils.isEmpty(cache)) {
            cache.forEach((key, value) -> put(key, value, timeout));
        }
    }

    public static void put(Map<String, Object> cache, LocalDateTime expireTime) {
        if (!CollectionUtils.isEmpty(cache)) {
            cache.forEach((key, value) -> put(key, value, expireTime));
        }
    }

    /**
     * 获取缓存
     *
     * @param key key
     * @return 缓存对象
     */
    public static Object get(String key) {
        return CACHE_DATA.get(key);
    }

    /**
     * 获取当前缓存中 所有的key
     *
     * @return key集合
     */
    public static Set<String> cacheKeys() {
        return CACHE_DATA.keySet();
    }

    public static Map<String, Object> allCache() {
        return CACHE_DATA;
    }

    /**
     * 判断缓存是否包含key
     *
     * @param key key
     * @return 是否包含
     */
    public boolean containKey(String key) {
        return CACHE_DATA.containsKey(key);
    }

    /**
     * 获取当前缓存大小
     *
     * @return 大小
     */
    public static int size() {
        return CACHE_DATA.size();
    }

    /**
     * 删除缓存
     *
     * @param key key
     */
    public static void remove(String key) {
        CACHE_DATA.remove(key);
    }

    /**
     * 清空所有缓存
     */
    public static void clear() {
        if (size() > 0) {
            CACHE_DATA.clear();
        }
    }
}
