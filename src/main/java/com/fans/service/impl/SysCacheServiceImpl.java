package com.fans.service.impl;

import com.fans.common.CacheKeyConstants;
import com.fans.common.RedisPool;
import com.fans.service.interfaces.SysCacheService;
import com.fans.utils.JsonMapper;
import com.fans.utils.ObjectSerializeUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * @ClassName SysCacheServiceImpl
 * @Description: 缓存服务实习层
 * @Author fan
 * @Date 2018-11-16 16:35
 * @Version 1.0
 **/
@Service("sysCacheService")
@Slf4j
public class SysCacheServiceImpl implements SysCacheService {
    @Resource(name = "redisPool")
    private RedisPool redisPool;

    @Override
    public void saveCache(CacheKeyConstants key, String value, Integer timeOut) {
        if (value == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            if (timeOut == 0) {
                shardedJedis.set(key.name(), value);
            } else {
                shardedJedis.setex(key.name(), timeOut, value);
            }
        } catch (Exception e) {
            log.error("save cache exception, keys:{}", key.name());
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public String getFromCache(CacheKeyConstants key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.get(key.name());
        } catch (Exception e) {
            log.error("get from cache exception, keys:{}", key.name());
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

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
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys));
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
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys));
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public <T> void saveTCache(CacheKeyConstants key, T value, Integer timeOut) {
        if (value == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            if (timeOut == 0) {
                shardedJedis.set(key.name().getBytes(), ObjectSerializeUtil.serialization(value));
            } else {
                shardedJedis.setex(key.name().getBytes(), timeOut, ObjectSerializeUtil.serialization(value));
            }
        } catch (Exception e) {
            log.error("save cache exception, keys:{}", key.name());
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public Object getTCache(CacheKeyConstants key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            byte[] result = shardedJedis.get(key.name().getBytes());
            return ObjectSerializeUtil.deserialization(result);
        } catch (Exception e) {
            log.error("get from cache exception, keys:{}", key.name());
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
                shardedJedis.set(key.getBytes(), ObjectSerializeUtil.serialization(value));
            } else {
                shardedJedis.setex(key.getBytes(), timeOut, ObjectSerializeUtil.serialization(value));
            }
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys));
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
            return ObjectSerializeUtil.deserialization(result);
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys));
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    @Override
    public void delCache(CacheKeyConstants key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            shardedJedis.del(key.name());
        } catch (Exception e) {
            log.error("del from cache exception, keys:{}", key.name());
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
            log.error("del from cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys));
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
