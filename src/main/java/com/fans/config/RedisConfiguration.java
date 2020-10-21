package com.fans.config;

import com.fans.conditionals.RedisConditional;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.List;

/**
 * className: RedisConfiguration
 *
 * @author k
 * @version 1.0
 * @description redis数据源配置
 * @date 2018-12-20 14:14
 **/
@Configuration
@Conditional(RedisConditional.class)
@Slf4j
public class RedisConfiguration {

    @Resource
    private RedisProperties redisProperties;


    private JedisPoolConfig assemble() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        config.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());
        return config;
    }

    @Bean
    public ShardedJedisPool getShardedJdsPool() {
        JedisPoolConfig config = assemble();
        List<JedisShardInfo> jdsInfoList = Lists.newArrayList();
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        if (cluster == null) {
            JedisShardInfo jedisShardInfo = new JedisShardInfo(redisProperties.getHost(), redisProperties.getPort(), (int) redisProperties.getTimeout().getSeconds() * 1000);
            jedisShardInfo.setPassword(redisProperties.getPassword());
            return new ShardedJedisPool(config, Lists.newArrayList(jedisShardInfo));
        }
        List<String> nodes = cluster.getNodes();
        if (nodes.isEmpty()) {
            JedisShardInfo jedisShardInfo = new JedisShardInfo(redisProperties.getHost(), redisProperties.getPort(), (int) redisProperties.getTimeout().getSeconds() * 1000);
            jedisShardInfo.setPassword(redisProperties.getPassword());
            return new ShardedJedisPool(config, Lists.newArrayList(jedisShardInfo));
        }
        nodes.forEach(host -> {
            String[] url = host.split(":");
            if (StringUtils.isBlank(url[1])) {
                url[1] = "6379";
            }
            JedisShardInfo jedisShardInfo = new JedisShardInfo(url[0], Integer.parseInt(url[1]), (int) redisProperties.getTimeout().getSeconds() * 1000);
            jedisShardInfo.setPassword(jedisShardInfo.getPassword());
            jdsInfoList.add(jedisShardInfo);
        });
        return new ShardedJedisPool(config, jdsInfoList);
    }

    @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig config = assemble();
        return new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(), (int) redisProperties.getTimeout().getSeconds() * 1000, redisProperties.getPassword(), redisProperties.getDatabase(), redisProperties.isSsl());
    }
}
