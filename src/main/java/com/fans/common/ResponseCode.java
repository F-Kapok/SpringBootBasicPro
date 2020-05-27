package com.fans.common;

import lombok.Getter;

/**
 * className: ResponseCode
 *
 * @author k
 * @version 1.0
 * @description 响应代码
 * @date 2018-12-20 14:14
 **/
@Getter
public enum ResponseCode {
    /**
     * 成功标识码 200
     */
    SUCCESS(200, "Response successfully"),
    /**
     * 失败标识码 500
     */
    ERROR(500, "Response error"),
    /**
     * 需要登录标识码 401
     */
    NEED_LOGIN(401, "Response need login"),
    /**
     * 参数不符合规定标识码 415
     */
    ILLEGAL_ARGUMENT(415, "Response argument illegality");

    private final Integer code;
    private final String desc;

    ResponseCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
