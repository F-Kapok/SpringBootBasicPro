package com.fans.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * className: ObjectSerializeUtils
 *
 * @author k
 * @version 1.0
 * @description 对象序列化工具类
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class ObjectSerializeUtils {

    /**
     * description: 将复杂对象序列化成字节
     *
     * @param obj 对象
     * @return byte[]
     * @author k
     * @date 2018/12/09 14:18
     **/
    public static <T> byte[] serialization(T obj) {
        ObjectOutputStream outputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(obj);
            outputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("ObjectSerializeUtils-IOException:{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * description: 将字节反序列化成为对象
     *
     * @param byteArray 字节数组
     * @return java.lang.Object
     * @author k
     * @date 2018/12/09 14:26
     **/
    public static Object deserialization(byte[] byteArray) {
        ObjectInputStream inputStream;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        try {
            inputStream = new ObjectInputStream(byteArrayInputStream);
            return inputStream.readObject();
        } catch (Exception e) {
            log.error("ObjectSerializeUtils-IOException:{}", e.getMessage(), e);
            return null;
        }
    }
}
