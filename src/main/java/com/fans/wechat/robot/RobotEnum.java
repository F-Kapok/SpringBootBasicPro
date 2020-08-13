package com.fans.wechat.robot;

import java.util.Arrays;

/**
 * enumName: RobotEnum
 *
 * @author k
 * @version 1.0
 * @description 机器人枚举
 * @date 2019-04-13 08:43
 **/
public enum RobotEnum {
    /**
     *
     */
    ROBOT_ONE(1, "d85ae60e-e1e4-4438-a083-10bd6e8c17b1", "小焕焕", "一生一世");

    private final Integer code;
    private final String key;
    private final String name;
    private final String desc;

    RobotEnum(Integer code, String key, String name, String desc) {
        this.code = code;
        this.key = key;
        this.name = name;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public static RobotEnum get(Integer code) {
        return Arrays.stream(RobotEnum.values())
                .filter(value -> value.getCode().equals(code))
                .findAny()
                .orElse(null);
    }

    public static RobotEnum get(String desc) {
        return Arrays.stream(RobotEnum.values())
                .filter(value -> value.getKey().equals(desc))
                .findAny()
                .orElse(null);
    }

    public static String[] getArray() {
        return Arrays.stream(RobotEnum.values())
                .map(RobotEnum::getKey)
                .toArray(String[]::new);
    }

}
