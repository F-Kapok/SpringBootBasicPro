package com.fans.utils;

import com.fans.common.CodeEnum;

import java.util.stream.Stream;

/**
 * className: EnumUtils
 *
 * @author k
 * @version 1.0
 * @description 枚举类描述信息获取
 * @date 2018-12-20 14:14
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
