package com.fans.wechat.robot.param;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * className: TextBody
 *
 * @author k
 * @version 1.0
 * @description 文本类型入参
 * @date 2019-04-13 08:43
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class TextBody extends BodyBase implements Serializable {

    private static final long serialVersionUID = -20200813232207L;

    /**
     * 文本内容，最长不超过2048个字节，必须是utf8编码 例如：hello world 必传
     */
    private String content;
    /**
     * userid列表，提醒群中指定成员  例如: @xx,@all 非必传
     * ["xx","@all"]
     */
    private List<String> mentioned_list;
    /**
     * 手机号列表，提醒手机号对应的群成员  例如: @18888888888,@all 非必传
     * ["18888888888","@all"]
     */
    private List<String> mentioned_mobile_list;

}
