package com.fans.wechat.robot.param;

import lombok.*;

import java.io.Serializable;

/**
 * className: NewsBody
 *
 * @author k
 * @version 1.0
 * @description 图文类型消息体入参
 * @date 2019-04-13 08:43
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class NewBody implements Serializable {

    private static final long serialVersionUID = 5441738592746146407L;
    /**
     * 标题，不超过128个字节，超过会自动截断 必传
     */
    private String title;
    /**
     * 描述，不超过512个字节，超过会自动截断 非必传
     */
    private String description;
    /**
     * 点击后跳转的链接。必传
     */
    private String url;
    /**
     * 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。非必传
     */
    private String picurl;
}
