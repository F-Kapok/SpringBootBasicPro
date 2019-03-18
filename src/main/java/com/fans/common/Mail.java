package com.fans.common;

import lombok.*;

/**
 * @ClassName Mail
 * @Description:
 * @Author fan
 * @Date 2019-03-18 17:26
 * @Version 1.0
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Mail {
    /**
     * 邮件接收人
     */
    private String recipient;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
}
