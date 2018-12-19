package com.fans.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName ApplicationContextHelper
 * @Description: TODO 获取spring上下文工具类
 * @Author fan
 * @Date 2018-11-06 13:06
 * @Version 1.0
 **/
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
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
