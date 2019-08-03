package com.fans.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
     * @Param: [key, time(当前时间+超时时间)]
     * @return: boolean 锁住返回true
     * @Author: fan
     * @Date: 2019/01/16 13:22
     **/
    @Transactional(rollbackFor = Exception.class)
    public boolean lock(String key, String time) {
        //setIfAbsent相当于setNX 返回boolean 不存在就直接上锁
        if (redisTemplate.opsForValue().setIfAbsent(key + SUFFIX, time)) {
            return true;
        }
        //存在则取出超时时间
        String currentTime = redisTemplate.opsForValue().get(key + SUFFIX);
        //如果锁超时 取出上一个锁的时间并更新时间（这里可以保证N个线程同时进来肯定会有一个线程拿到锁）
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
