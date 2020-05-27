package com.fans.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * className: ApplicationContextHelper
 *
 * @author k
 * @version 1.0
 * @description 获取spring上下文工具类
 * @date 2018-12-20 14:14
 **/
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static Object popBean(String beanName) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanName);
    }

    public static <T> T popBean(Class<T> tClass) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(tClass);
    }

    public static <T> T popBean(String beanName, Class<T> tClass) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanName, tClass);
    }
}
