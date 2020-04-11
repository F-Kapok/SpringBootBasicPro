package com.fans.utils;

import com.fans.common.CodeEnum;

import java.util.stream.Stream;

/**
 * @ClassName EnumUtils
 * @Description: 枚举类描述信息获取
 * @Author fan
 * @Date 2019-03-29 09:08
 * @Version 1.0
 **/
public class EnumUtils {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
//        for (T enumConstant : enumClass.getEnumConstants()) {
//            if (enumConstant.getCode().equals(code)) {
//                return enumConstant;
//            }
//        }
//        return null;
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}
