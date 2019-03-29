package com.fans.utils;

import com.fans.common.CodeEnum;

/**
 * @ClassName EnumUtils
 * @Description: 枚举类描述信息获取
 * @Author fan
 * @Date 2019-03-29 09:08
 * @Version 1.0
 **/
public class EnumUtils {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.getCode().equals(code)) {
                return enumConstant;
            }
        }
        return null;
    }
}
