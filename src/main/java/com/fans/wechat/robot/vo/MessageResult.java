package com.fans.wechat.robot.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

/**
 * className: MessageResult
 *
 * @author k
 * @version 1.0
 * @description 消息返回结果
 * @date 2019-04-13 08:43
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MessageResult implements Serializable {

    private static final long serialVersionUID = -20200813231720L;

    private Integer errcode;
    private String errmsg;

    @JsonIgnore
    public boolean isSuccess() {
        return errcode == 0;
    }
    @JsonIgnore
    public Integer getCode() {
        return errcode;
    }
    @JsonIgnore
    public String getMessage() {
        return errmsg;
    }
}
