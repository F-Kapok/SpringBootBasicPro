package com.fans.wechat.robot.param;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * className: NewsBody
 *
 * @author k
 * @version 1.0
 * @description 图文类型消息入参
 * @date 2019-04-13 08:43
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class NewsBody extends BodyBase implements Serializable {

    private static final long serialVersionUID = 2834446052474793974L;
    /**
     * 图文消息，一个图文消息支持1到8条图文
     */
    private List<NewBody> articles;
}
