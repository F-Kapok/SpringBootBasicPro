package com.fans.common;

import lombok.Getter;

/**
 * @EnumName UserStatusEnum
 * @Description: 用户状态枚举类
 * @Author fan
 * @Date 2019-03-29 09:19
 * @Version 1.0
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

    private Integer code;
    private String desc;

    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }}
