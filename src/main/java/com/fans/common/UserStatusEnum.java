package com.fans.common;

import lombok.Getter;

/**
 * className: UserStatusEnum
 *
 * @author k
 * @version 1.0
 * @description 用户状态枚举类
 * @date 2018-12-20 14:14
 **/
@Getter
public enum UserStatusEnum implements CodeEnum {
    /**
     * 停用
     */
    STEWARD_INFO_STATUS_FAIL(0, "停用"),
    /**
     * 启用
     */
    STEWARD_INFO_STATUS_SUCCESS(1, "启用"),
    /**
     * 到期
     */
    STEWARD_INFO_STATUS_TIMEOUT(2, "到期");

    private final Integer code;
    private final String desc;

    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
