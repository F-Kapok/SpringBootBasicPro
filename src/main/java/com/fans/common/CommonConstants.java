package com.fans.common;

/**
 * @ClassName CommonConstants
 * @Description: TODO 系统通用字段
 * @Author fan
 * @Date 2018-12-20 13:56
 * @Version 1.0
 **/
public class CommonConstants {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role {
        /**
         * 普通用户
         */
        Integer ROLE_CUSTOMER = 0;
        /**
         * 管理员
         */
        Integer ROLE_ADMIN = 1;
    }
}
