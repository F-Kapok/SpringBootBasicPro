package com.fans.conditionals;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * className: RedisConditional
 *
 * @author k
 * @version 1.0
 * @description
 * @date 2020-10-22 00:04
 **/
public class RedisConditional implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        String redisHost = environment.getProperty("spring.redis.host");
        return StringUtils.isNotBlank(redisHost);
    }
}
