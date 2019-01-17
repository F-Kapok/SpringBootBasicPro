package com.fans.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName RedisSynchronize
 * @Description: redis分布式锁
 * @Author fan
 * @Date 2019-01-16 13:08
 * @Version 1.0
 **/
@Component(value = "redisSynchronize")
@Slf4j
public class RedisSynchronize {
    private static final String SUFFIX = "_lock";
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    /**
     * @Description: 加锁
     * @Param: [key, time]
     * @return: boolean
     * @Author: fan
     * @Date: 2019/01/16 13:22
     **/
    public boolean lock(String key, String time) {
        if (redisTemplate.opsForValue().setIfAbsent(key + SUFFIX, time)) {
            return true;
        }
        String currentTime = redisTemplate.opsForValue().get(key + SUFFIX);
        if (StringUtils.isNotBlank(currentTime) && Long.parseLong(currentTime) < System.currentTimeMillis()) {
            String oldTime = redisTemplate.opsForValue().getAndSet(key + SUFFIX, time);
            if (StringUtils.isNotBlank(oldTime) && oldTime.equals(currentTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 解锁
     * @Param: [key, time]
     * @return: void
     * @Author: fan
     * @Date: 2019/01/16 13:29
     **/
    public void unlock(String key, String time) {
        try {
            String currentTime = redisTemplate.opsForValue().get(key + SUFFIX);
            if (StringUtils.isNotBlank(currentTime) && currentTime.equals(time)) {
                redisTemplate.opsForValue().getOperations().delete(key + SUFFIX);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】解锁失败，{}", e);
        }
    }
}
