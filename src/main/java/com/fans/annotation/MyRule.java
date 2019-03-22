package com.fans.annotation;

import com.fans.validator.MyRuleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyRuleValidator.class)
public @interface MyRule {
    String message() default "我的默认规则";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
