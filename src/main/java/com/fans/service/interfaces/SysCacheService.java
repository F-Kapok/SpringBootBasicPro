package com.fans.service.interfaces;


import com.fans.common.CacheKeyConstants;

/**
 * @InterfaceName SysCacheService
 * @Description: 缓存服务接口
 * @Author fan
 * @Date 2018-11-16 16:31
 * @Version 1.0
 **/
public interface SysCacheService {

    void saveCache(CacheKeyConstants key, String value, Integer timeOut);

    String getFromCache(CacheKeyConstants key);

    void saveCache(CacheKeyConstants prefix, String value, Integer timeOut, String... keys);

    String getFromCache(CacheKeyConstants prefix, String... keys);

    <T> void saveTCache(CacheKeyConstants key, T value, Integer timeOut);

    Object getTCache(CacheKeyConstants key);

    <T> void saveTCache(CacheKeyConstants prefix, T value, Integer timeOut, String... keys);

    Object getTCache(CacheKeyConstants prefix, String... keys);

    void delCache(CacheKeyConstants key);

    void delCache(CacheKeyConstants prefix, String... keys);

}
