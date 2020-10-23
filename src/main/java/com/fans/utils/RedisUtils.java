package com.fans.utils;

import com.fans.common.CacheKeyConstants;
import com.fans.common.RedisPool;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Map;

/**
 * className: RedisUtil
 *
 * @author k
 * @version 1.0
 * @description 缓存工具集合
 * @date 2020-10-21 23:13
 **/
@Slf4j
public class RedisUtils {

    private static final RedisPool REDIS_POOL;

    static {
        REDIS_POOL = ApplicationContextHelper.popBean("redisPool", RedisPool.class);
    }

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
    public static void saveCache(CacheKeyConstants prefix, String value, Integer timeOut, String... keys) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String key = generateCacheKey(prefix, keys);
            shardedJedis = REDIS_POOL.instance();
            if (timeOut == 0) {
                shardedJedis.set(key, value);
            } else {
                shardedJedis.setex(key, timeOut, value);
            }
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
    }

    /**
     * description: 根据拼接的key获取String类型缓存值
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            return shardedJedis.get(key);
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
            return null;
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
    }

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
    public static <T> void saveTCache(CacheKeyConstants prefix, T value, Integer timeOut, String... keys) {
        if (value == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String key = generateCacheKey(prefix, keys);
            shardedJedis = REDIS_POOL.instance();
            if (timeOut == 0) {
                shardedJedis.set(key.getBytes(), ObjectSerializeUtils.serialization(value));
            } else {
                shardedJedis.setex(key.getBytes(), timeOut, ObjectSerializeUtils.serialization(value));
            }
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
    }

    /**
     * description: 根据拼接的key获取对象缓存值
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static Object getTCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            byte[] result = shardedJedis.get(key.getBytes());
            return ObjectSerializeUtils.deserialization(result);
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
            return null;
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
    }

    /**
     * description: 根据拼接key删除缓存
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static void delCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            shardedJedis.del(key);
        } catch (Exception e) {
            log.error("del from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
    }

    /**
     * description: 递增
     *
     * @param prefix    前缀
     * @param increment 递增幅度
     * @param keys      拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static void increment(CacheKeyConstants prefix, long increment, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            shardedJedis.incrBy(key, increment);
        } catch (Exception e) {
            log.error("incr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
    }

    public static Long incrementAndGet(CacheKeyConstants prefix, long increment, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            return shardedJedis.incrBy(key, increment);
        } catch (Exception e) {
            log.error("incr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
        return -1L;
    }

    public static Long getAndIncrement(CacheKeyConstants prefix, long increment, String... keys) {
        return incrementAndGet(prefix, increment, keys) - increment;
    }

    /**
     * description: 递减
     *
     * @param prefix    前缀
     * @param decrement 递减幅度
     * @param keys      拼接key
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static void decrement(CacheKeyConstants prefix, long decrement, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            shardedJedis.decrBy(key, decrement);
        } catch (Exception e) {
            log.error("decr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
    }

    public static Long decrementAndGet(CacheKeyConstants prefix, long decrement, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            return shardedJedis.decrBy(key, decrement);
        } catch (Exception e) {
            log.error("decr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
        return -1L;
    }

    public static Long getAndDecrement(CacheKeyConstants prefix, long decrement, String... keys) {
        return decrementAndGet(prefix, decrement, keys) + decrement;
    }

    /**
     * description: 判断key是否存在redis中
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @return boolean
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static boolean exists(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            return shardedJedis.exists(key);
        } catch (Exception e) {
            log.error("decr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
        return false;
    }

    /**
     * description: 批量获取 此方法不适用于集群
     *
     * @param keyList key集合
     * @return java.util.List<java.lang.String> 结果与 key集合排序一一对应
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static Map<String, String> multiGet(List<String> keyList) {
        Jedis jedis = null;
        try {
            if (keyList == null || keyList.isEmpty()) {
                return null;
            }
            jedis = REDIS_POOL.instanceJedis();
            List<String> cacheResult = jedis.mget(keyList.toArray(new String[0]));
            Map<String, String> result = Maps.newHashMap();
            for (int i = 0; i < keyList.size(); i++) {
                result.put(keyList.get(i), cacheResult.get(i));
            }
            return result;
        } catch (Exception e) {
            log.error("multiGet from cache exception,keyList:{}", keyList.toString());
            log.error(e.getMessage(), e);
            return null;
        } finally {
            REDIS_POOL.safeClose(jedis);
        }
    }

    /**
     * description: 查看过期时间
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @return java.lang.Long
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static Long ttl(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            return shardedJedis.ttl(key);
        } catch (Exception e) {
            log.error("decr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
        return 0L;
    }


    /**
     * description: 设置过期时间
     *
     * @param prefix 前缀
     * @param keys   拼接key
     * @return java.lang.Long
     * @author k
     * @date 2018-11-16 16:31
     **/
    public static void setExpire(CacheKeyConstants prefix, Integer seconds, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = REDIS_POOL.instance();
            shardedJedis.expire(key, seconds);
        } catch (Exception e) {
            log.error("decr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            log.error(e.getMessage(), e);
        } finally {
            REDIS_POOL.safeClose(shardedJedis);
        }
    }

    public static String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += ":" + Joiner.on(":").join(keys);
        }
        return key;
    }

}
