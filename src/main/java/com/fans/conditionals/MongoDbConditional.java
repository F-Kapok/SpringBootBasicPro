package com.fans.conditionals;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * className: MongoDbConditional
 *
 * @author k
 * @version 1.0
 * @description
 * @date 2020-10-21 23:07
 **/
public class MongoDbConditional implements Condition {


    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        String mongoDbUri = environment.getProperty("spring.data.mongodb.uri");
        return StringUtils.isNotBlank(mongoDbUri);
    }
}
