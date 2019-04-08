package com.fans.utils;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName ObjectUtils
 * @Description: 对象操作工具类
 * @Author fan
 * @Date 2019-04-08 15:20
 * @Version 1.0
 **/
@Slf4j
public class ObjectUtils {
    /**
     * @Description: Map集合转换响应的Object对象
     * @Param: [map, beanClass]
     * @return: java.lang.Object
     * @Author: fan
     * @Date: 2019/04/08 15:31
     **/
    public static Object map2Object(Map<String, Object> map, Class<?> beanClass) {
        if (map.isEmpty()) {
            return null;
        }
        Object obj = null;
        try {
            obj = beanClass.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method setter = propertyDescriptor.getWriteMethod();
                if (setter != null) {
                    setter.invoke(obj, map.get(propertyDescriptor.getName()));
                }
            }
        } catch (Exception e) {
            log.error("--> map2Object fail:{}", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * @Description: Object转换为Map集合
     * @Param: [obj]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: fan
     * @Date: 2019/04/08 15:31
     **/
    public static Map<String, Object> object2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = null;
        try {
            map = Maps.newHashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String key = propertyDescriptor.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = propertyDescriptor.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                map.put(key, value);
            }
        } catch (Exception e) {
            log.error("--> object2Map fail:{}", e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @Description: 对象比较 是否相等
     * @Param: [o1, o2]
     * @return: boolean
     * @Author: fan
     * @Date: 2019/04/08 15:37
     **/
    public static boolean equals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }
}
