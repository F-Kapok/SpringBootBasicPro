package com.fans.service.interfaces;


import com.fans.common.CacheKeyConstants;

/**
 * interfaceName: SysCacheService
 *
 * @author k
 * @version 1.0
 * @description 缓存服务接口
 * @date 2018-11-16 16:31
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
