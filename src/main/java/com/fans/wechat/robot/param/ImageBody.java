package com.fans.wechat.robot.param;

import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * className: ImageBody
 *
 * @author k
 * @version 1.0
 * @description 图片类型消息入参
 * @date 2019-04-13 08:43
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ImageBody extends BodyBase implements Serializable {

    private static final long serialVersionUID = 8251417234093918622L;

    /**
     * 图片内容的base64编码
     */
    private String base64;
    /**
     * 图片内容（base64编码前）的md5值
     */
    private String md5;


    public static String getMd5(String path) {
        try {
            return DigestUtils.md5Hex(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getBase64(String path) {
        byte[] data = null;
        try (InputStream inputStream = new FileInputStream(path)) {
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
}
