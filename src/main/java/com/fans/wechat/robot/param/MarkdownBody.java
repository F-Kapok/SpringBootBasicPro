package com.fans.wechat.robot.param;

import lombok.*;

import java.io.Serializable;

/**
 * className: MarkdownBody
 *
 * @author k
 * @version 1.0
 * @description markdown类型消息入参
 * @date 2019-04-13 08:43
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MarkdownBody extends BodyBase implements Serializable {

    private static final long serialVersionUID = -20200814003635L;

    /**
     * markdown内容，最长不超过4096个字节，必须是utf8编码
     * 目前支持的markdown语法是如下的子集：
     * <p>
     * 1.标题 （支持1至6级标题，注意#与文字中间要有空格）
     * # 标题一
     * ## 标题二
     * ### 标题三
     * #### 标题四
     * ##### 标题五
     * ###### 标题六
     * <p>
     * 2.加粗
     * **bold**
     * <p>
     * 3.链接
     * [这是一个链接](http://work.weixin.qq.com/api/doc)
     * 4.行内代码段（暂不支持跨行）
     * `code`
     * <p>
     * 5.引用
     * > 引用文字
     * <p>
     * 6.字体颜色(只支持3种内置颜色)
     * <font color="info">绿色</font>
     * <font color="comment">灰色</font>
     * <font color="warning">橙红色</font>
     */
    private String content;

}
