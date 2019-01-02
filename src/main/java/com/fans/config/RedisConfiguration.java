package com.fans.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName RedisConfiguration
 * @Description: TODO redis数据源配置
 * @Author fan
 * @Date 2018-12-20 15:15
 * @Version 1.0
 **/
@Configuration
@Slf4j
public class RedisConfiguration {
    @Resource
    private RedisProperties properties;

    private JedisPoolConfig assemble() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        config.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        config.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());
        return config;
    }

   @Bean
    public ShardedJedisPool getShardedJdsPool() {
        JedisPoolConfig config = assemble();
        List<JedisShardInfo> jdsInfoList = Lists.newArrayList();
        String[] hosts = properties.getHost().split(",");
        for (String host : hosts) {
            jdsInfoList.add(new JedisShardInfo(host));
        }
        return new ShardedJedisPool(config, jdsInfoList);
    }
}
