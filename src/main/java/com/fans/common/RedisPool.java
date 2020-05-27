package com.fans.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * className: RedisPool
 *
 * @author k
 * @version 1.0
 * @description redis 使用类
 * @date 2018-12-20 14:14
 **/
@Component("redisPool")
@Slf4j
public class RedisPool {
    @Resource
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis instance() {

        return shardedJedisPool.getResource();
    }

    public void safeClose(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            log.error("return redis resource exception", e);
        }
    }
}
