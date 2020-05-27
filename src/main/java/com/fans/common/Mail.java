package com.fans.common;

import lombok.*;

/**
 * className: Mail
 *
 * @author k
 * @version 1.0
 * @description 控制层AOP
 * @date 2018-12-20 14:14
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
