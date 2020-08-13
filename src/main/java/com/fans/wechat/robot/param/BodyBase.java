package com.fans.wechat.robot.param;

import java.io.Serializable;

/**
 * className: BodyBase
 *
 * @author k
 * @version 1.0
 * @description 基础消息体
 * @date 2019-04-13 08:43
 **/
public class BodyBase implements Serializable {

    private static final long serialVersionUID = -20200814000421L;

    public static final String KEY = "msgtype";
    public static final String TEXT = "text";
    public static final String MARKDOWN = "markdown";
    public static final String IMAGE = "image";
    public static final String NEWS = "news";
    public static final String FILE = "file";

}
