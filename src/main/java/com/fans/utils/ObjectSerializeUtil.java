package com.fans.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @ClassName ObjectSerializeUtil
 * @Description: TODO 对象序列化工具类
 * @Author fan
 * @Date 2018-12-09 14:14
 * @Version 1.0
 **/
@Slf4j
public class ObjectSerializeUtil {
    /**
     * @Description: TODO 将复杂对象序列化成字节
     * @Param: [obj]
     * @return: byte[]
     * @Author: fan
     * @Date: 2018/12/09 14:18
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
            log.error("ObjectSerializeUtil-IOException:{}", e);
            return null;
        }
    }

    /**
     * @Description: TODO 将字节反序列化成为对象
     * @Param: [byteArray]
     * @return: java.lang.Object
     * @Author: fan
     * @Date: 2018/12/09 14:26
     **/
    public static Object deserialization(byte[] byteArray) {
        ObjectInputStream inputStream;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        try {
            inputStream = new ObjectInputStream(byteArrayInputStream);
            return inputStream.readObject();
        } catch (Exception e) {
            log.error("ObjectSerializeUtil-IOException:{}", e);
            return null;
        }
    }
}
