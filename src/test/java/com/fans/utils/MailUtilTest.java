package com.fans.utils;

import com.fans.common.Mail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MailUtilTest {
    
    @Resource(name = "mailUtil")
    private MailUtil mailUtil;

    @Test
    public void sendSimpleMail() {
        String recipient = "279366089@qq.com";
        Mail mail = Mail.builder()
                .recipient(recipient)
                .subject("测试邮件")
                .content("测试")
                .build();
        mailUtil.sendSimpleMail(mail);
    }

    @Test
    public void sendHTMLMail() {
    }

    @Test
    public void sendAttachmentMail() {
    }

    @Test
    public void sendInlineMail() {
    }

    @Test
    public void sendTemplateMail() {
    }
}