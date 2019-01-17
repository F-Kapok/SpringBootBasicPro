package com.fans.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JsonData
 * @Description: 自定义交互数据定义类
 * @Author fan
 * @Date 2018-11-20 09:44
 * @Version 1.0
 **/
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonData<T> implements Serializable {

    private static final long serialVersionUID = 123456789L;

    private Integer flag;

    private String msg;

    private T data;

    private JsonData(Integer flag) {
        this.flag = flag;
    }

    private JsonData(Integer flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    private JsonData(Integer flag, T data) {
        this.flag = flag;
        this.data = data;
    }

    private JsonData(Integer flag, String msg, T data) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }

    public static <T> JsonData<T> success(String msg, T data) {
        return new JsonData<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> JsonData<T> success(T data) {
        return new JsonData<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> JsonData<T> success(String msg) {
        return new JsonData<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> JsonData<T> success() {
        return new JsonData<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> JsonData<T> fail(String msg) {
        return new JsonData<>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> JsonData<T> failCodeMsg(Integer flag, String msg) {
        return new JsonData<>(flag, msg);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.flag.equals(ResponseCode.SUCCESS.getCode());
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("flag", flag);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }
}
