package com.fans.common;

import lombok.Getter;

/**
 * @EnumName ResponseCode
 * @Description: TODO 响应代码
 * @Author fan
 * @Date 2018-11-23 11:29
 * @Version 1.0
 **/
@Getter
public enum ResponseCode {
    /**
     * 成功标识码 0
     */
    SUCCESS(0, "Response successfully"),
    /**
     * 失败标识码 1
     */
    ERROR(1, "Response error"),
    /**
     * 需要登录标识码 10
     */
    NEED_LOGIN(10, "Response need login"),
    /**
     * 参数不符合规定标识码 2
     */
    ILLEGAL_ARGUMENT(2, "Response argument illegality");

    private final Integer code;
    private final String desc;

    ResponseCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
