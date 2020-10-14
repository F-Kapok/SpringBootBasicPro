package com.fans.service.interfaces;


import com.fans.common.CacheKeyConstants;

import java.util.List;
import java.util.Map;

/**
 * interfaceName: SysCacheService
 *
 * @author k
 * @version 1.0
 * @description 缓存服务接口
 * @date 2018-11-16 16:31
 **/
public interface SysCacheService {

    /**
     * description: 存储String缓存，有时间限制 可使用":"符号拼接key 也可将前缀直接当做key
     *
     * @param prefix  前缀
     * @param value   String值
     * @param timeOut 时间单位 秒 为 0则不过期
     * @param keys    拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    void saveCache(CacheKeyConstants prefix, String value, Integer timeOut, String... keys);

    /**
     * description: 根据拼接的key获取String类型缓存值
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    String getFromCache(CacheKeyConstants prefix, String... keys);

    /**
     * description: 存储对象缓存，有时间限制 可使用":"符号拼接key 也可将前缀直接当做key
     *
     * @param prefix  前缀
     * @param value   String值
     * @param timeOut 时间单位 秒 为 0则不过期
     * @param keys    拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    <T> void saveTCache(CacheKeyConstants prefix, T value, Integer timeOut, String... keys);

    /**
     * description: 根据拼接的key获取对象缓存值
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    Object getTCache(CacheKeyConstants prefix, String... keys);

    /**
     * description: 根据拼接key删除缓存
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    void delCache(CacheKeyConstants prefix, String... keys);

    /**
     * description: 递增
     *
     * @param prefix    前缀
     * @param increment 递增幅度
     * @param keys      拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    void increment(CacheKeyConstants prefix, long increment, String... keys);

    /**
     * description: 递减
     *
     * @param prefix    前缀
     * @param decrement 递减幅度
     * @param keys      拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    void decrement(CacheKeyConstants prefix, long decrement, String... keys);

    /**
     * description: 判断key是否存在redis中
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @return boolean
     * @author k
     * @date 2018-11-16 16:31
     **/
    boolean exists(CacheKeyConstants prefix, String... keys);

    /**
     * description: 批量获取 此方法不适用于集群
     *
     * @param keyList key集合
     * @return java.util.List<java.lang.String> 结果与 key集合排序一一对应
     * @author k
     * @date 2020/10/14 14:21
     **/
    Map<String, String> multiGet(List<String> keyList);


}
