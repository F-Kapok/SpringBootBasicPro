package com.fans.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.STRING;

/**
 * className: JsonUtils
 *
 * @author k
 * @version 1.0
 * @description 对象与字符串转换工具
 * @date 2018-11-06 12:41
 **/
@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 初始化 initialize 去除掉对getter和setter的依赖
        objectMapper.setVisibility(ALL, NONE)
                .setVisibility(FIELD, ANY);
    }

    public static <T> String obj2String(T src) {
        if (src == null) {
            return null;
        }
        try {
            if (src instanceof String) {
                return (String) src;
            } else {
                return objectMapper.writeValueAsString(src);
            }
        } catch (Exception e) {
            log.warn("parse object to String exception, error:{}", e.getMessage(), e);
            return null;
        }
    }

    public static <T> String obj2FormattingString(T src) {
        if (src == null) {
            return null;
        }
        return prettyPrint(obj2String(src));
    }

    public static <T> T string2Obj(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null) {
            return null;
        }
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

    public static <T> T string2Obj(String src, Class<T> objectClass) {
        if (src == null || objectClass == null) {
            return null;
        }
        try {
            if (StringUtils.equals(objectClass.getCanonicalName(), STRING)) {
                return (T) src;
            } else {
                return objectMapper.readValue(src, objectClass);
            }
        } catch (Exception e) {
            log.warn("parse String to Object exception, String:{}, Class<T>:{}, error:{}", src, objectClass.getCanonicalName(), e);
            return null;
        }
    }

    /**
     * description: 格式化JSON字符串
     *
     * @param unformattedJsonString 未格式化的json字符
     * @return java.lang.String
     * @author k
     * @date 2018-11-06 12:41
     **/
    private static String prettyPrint(String unformattedJsonString) {
        StringBuilder sb = new StringBuilder();
        int indentLevel = 0;
        boolean inQuote = false;
        for (char charFromUnformattedJson : unformattedJsonString.toCharArray()) {
            switch (charFromUnformattedJson) {
                case '"':
                    // switch the quoting status
                    inQuote = !inQuote;
                    sb.append(charFromUnformattedJson);
                    break;
                case ' ':
                    // For space: ignore the space if it is not being quoted.
                    if (inQuote) {
                        sb.append(charFromUnformattedJson);
                    }
                    break;
                case '{':
                case '[':
                    // Starting a new block: increase the indent level
                    sb.append(charFromUnformattedJson);
                    indentLevel++;
                    appendIndentedNewLine(indentLevel, sb);
                    break;
                case '}':
                case ']':
                    // Ending a new block; decrese the indent level
                    indentLevel--;
                    appendIndentedNewLine(indentLevel, sb);
                    sb.append(charFromUnformattedJson);
                    break;
                case ',':
                    // Ending a json item; create a new line after
                    sb.append(charFromUnformattedJson);
                    if (!inQuote) {
                        appendIndentedNewLine(indentLevel, sb);
                    }
                    break;
                default:
                    sb.append(charFromUnformattedJson);
            }
        }
        return sb.toString();
    }

    /**
     * 在新行开始处用缩进
     *
     * @param indentLevel
     * @param stringBuilder
     */
    /**
     * description:
     *
     * @param indentLevel   压等级
     * @param stringBuilder 字符串拼接
     * @author k
     * @date 2018-11-06 12:41
     **/
    private static void appendIndentedNewLine(int indentLevel, StringBuilder stringBuilder) {
        stringBuilder.append("\n");
        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append("  ");
        }
    }
}
