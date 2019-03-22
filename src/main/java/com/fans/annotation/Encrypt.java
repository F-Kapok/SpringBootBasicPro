package com.fans.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface Encrypt {
    /**
     * 是否加密
     *
     * @return
     */
    boolean encode() default true;
}
