package com.fans.service.impl;

import com.fans.common.CacheKeyConstants;
import com.fans.common.RedisPool;
import com.fans.service.interfaces.SysCacheService;
import com.fans.utils.JsonUtils;
import com.fans.utils.ObjectSerializeUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * className: SysCacheServiceImpl
 *
 * @author k
 * @version 1.0
 * @description 缓存服务实习层
 * @date 2018-11-16 16:35
 **/
@Service("sysCacheService")
@Slf4j
public class SysCacheServiceImpl implements SysCacheService {
    @Resource(name = "redisPool")
    private RedisPool redisPool;

    @Override
    public void saveCache(CacheKeyConstants prefix, String value, Integer timeOut, String... keys) {
        if (value == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String key = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            if (timeOut == 0) {
                shardedJedis.set(key, value);
            } else {
                shardedJedis.setex(key, timeOut, value);
            }
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.get(key);
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public <T> void saveTCache(CacheKeyConstants prefix, T value, Integer timeOut, String... keys) {
        if (value == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String key = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            if (timeOut == 0) {
                shardedJedis.set(key.getBytes(), ObjectSerializeUtils.serialization(value));
            } else {
                shardedJedis.setex(key.getBytes(), timeOut, ObjectSerializeUtils.serialization(value));
            }
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public Object getTCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            byte[] result = shardedJedis.get(key.getBytes());
            return ObjectSerializeUtils.deserialization(result);
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public void delCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            shardedJedis.del(key);
        } catch (Exception e) {
            log.error("del from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public void increment(CacheKeyConstants prefix, long increment, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            shardedJedis.incrBy(key, increment);
        } catch (Exception e) {
            log.error("incr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public void decrement(CacheKeyConstants prefix, long decrement, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            shardedJedis.decrBy(key, decrement);
        } catch (Exception e) {
            log.error("decr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public boolean exists(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String key = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.exists(key);
        } catch (Exception e) {
            log.error("decr from cache exception, prefix:{}, keys:{}", prefix.name(), JsonUtils.obj2String(keys));
        } finally {
            redisPool.safeClose(shardedJedis);
        }
        return false;
    }

    @Override
    public Map<String, String> multiGet(List<String> keyList) {
        Jedis jedis = null;
        try {
            if (keyList == null || keyList.isEmpty()) {
                return null;
            }
            jedis = redisPool.instanceJedis();
            List<String> cacheResult = jedis.mget(Arrays.toString(keyList.toArray()));
            Map<String, String> result = Maps.newHashMap();
            for (int i = 0; i < keyList.size(); i++) {
                result.put(keyList.get(i), cacheResult.get(i));
            }
            return result;
        } catch (Exception e) {
            log.error("multiGet from cache exception,keyList:{}", keyList.toString());
            return null;
        } finally {
            redisPool.safeClose(jedis);
        }
    }

    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += ":" + Joiner.on(":").join(keys);
        }
        return key;
    }
}
