package com.fans.utils;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
 * @ClassName JsonMapper
 * @Description: TODO 对象与字符串转换工具
 * @Author fan
 * @Date 2018-11-06 12:41
 * @Version 1.0
 **/
@Slf4j
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //TODO 初始化 initialize
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    public static <T> String obj2String(T src) {
        if (src == null)
            return null;
        try {
            if (src instanceof String) {
                return (String) src;
            } else {
                return objectMapper.writeValueAsString(src);
            }
        } catch (Exception e) {
            log.warn("parse object to String exception, error:{}", e);
            return null;
        }
    }

    public static <T> T string2Obj(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null)
            return null;
        try {
            if (typeReference.getType().equals(String.class)) {
                return (T) src;
            } else {
                return objectMapper.readValue(src, typeReference);
            }
        } catch (Exception e) {
            log.warn("parse String to Object exception, String:{}, TypeReference<T>:{}, error:{}", src, typeReference.getType(), e);
            return null;
        }
    }
}
