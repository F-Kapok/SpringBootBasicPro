package com.fans.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @ClassName RedisPool
 * @Description: TODO redis 使用类
 * @Author fan
 * @Date 2018-11-16 16:27
 * @Version 1.0
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
