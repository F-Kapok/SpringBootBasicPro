package com.fans.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.google.common.collect.Maps;
import com.rits.cloning.Cloner;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * className: ObjectUtils
 *
 * @author k
 * @version 1.0
 * @description 对象操作工具类
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class ObjectUtils {

    /**
     * 对象拷贝类
     */
    private static final Cloner CLONER = new Cloner();
    /**
     * 对象拷贝（深）赋值--可拷贝对象
     */
    private static final Mapper MAPPER = DozerBeanMapperBuilder.buildDefault();

    /**
     * description: Map集合转换响应的Object对象
     *
     * @param map       集合
     * @param beanClass 实体类
     * @return java.lang.Object
     * @author k
     * @date 2019/04/08 15:31
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
     * description: Object转换为Map集合
     *
     * @param obj 实体
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author k
     * @date 2019/04/08 15:31
     **/
    public static Map<String, Object> object2Map(Object obj) {
        if (obj == null) {
            return Maps.newHashMap();
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
     * description: 对象比较 是否相等
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return boolean
     * @author k
     * @date 2020/05/27 23:06
     **/
    public static boolean equals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    /**
     * description: 对象深拷贝  可用于集合
     *
     * @param object 对象
     * @return T
     * @author k
     * @date 2020/05/27 23:06
     **/
    public static <T extends Serializable> T clone(T object) {
        T cloneObj = null;
        try {
            //写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(object);
            oos.close();

            //分配内存，写入原始对象，生成新的对象
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(in);
            //返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    /**
     * description: 工具类对象深拷贝
     *
     * @param object 要拷贝的对象
     * @return T
     * @author k
     * @date 2020/05/30 20:21
     **/
    public static <T> T clone(T object) {
        return CLONER.deepClone(object);
    }

    /**
     * description: 对象深赋值--可赋值对象，变量名必须相同
     *
     * @param source 数据来源
     * @param tClass 要拷贝到的对象源类
     * @return T
     * @author k
     * @date 2020/05/30 20:28
     **/
    public static <T> T copyProperties(Object source, Class<T> tClass) {
        return MAPPER.map(source, tClass);
    }

}
