package com.fans.utils;

import com.fans.common.Mail;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Map;


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
        String recipient = "279366089@qq.com";
        String htmlTemplate = "<h1>Springboot测试邮件HTML</h1>" +
                "<p style='color:#F00'>邮件测试用例</p>" +
                "<p style='text-align:right'>右对齐</p>";
        Mail mail = Mail.builder()
                .recipient(recipient)
                .subject("测试邮件")
                .content(htmlTemplate)
                .build();

        mailUtil.sendHTMLMail(mail);
    }

    @Test
    public void sendAttachmentMail() {
        String recipient = "279366089@qq.com";
        Mail mail = Mail.builder()
                .recipient(recipient)
                .subject("测试邮件")
                .content("测试")
                .build();
        String pathName = "src/main/resources/static/image/kapok.jpg";
        mailUtil.sendAttachmentMail(mail, pathName);
    }

    @Test
    public void sendInlineMail() {
        String recipient = "279366089@qq.com";
        String contentId = "picture";
        Mail mail = Mail.builder()
                .recipient(recipient)
                .subject("测试邮件")
                .content("<html><body>带静态资源的邮件内容，" +
                        "这个一张IDEA配置的照片:" +
                        "<img src='cid:picture' />" +
                        "</body></html>")
                .build();
        String pathName = "src/main/resources/static/image/kapok.jpg";
        mailUtil.sendInlineMail(mail, contentId, pathName);
    }

    @Test
    public void sendFreemarkerTemplateMail() {
        String recipient = "279366089@qq.com";
        Mail mail = Mail.builder()
                .recipient(recipient)
                .subject("测试邮件")
                .content("kapok")
                .build();
        Map<String, Object> model = Maps.newHashMap();
        model.put("username", mail.getContent());
        model.put("title", "标题Mail中使用了FreeMarker");
        String templateName = "/mail/freemarkerMail.ftl";
        mailUtil.sendFreemarkerTemplateMail(mail, model, templateName);
    }

    @Test
    public void sendThymeleafTemplateMail() {
        String recipient = "279366089@qq.com";
        Mail mail = Mail.builder()
                .recipient(recipient)
                .subject("测试邮件")
                .content("kapok")
                .build();
        Map<String, Object> model = Maps.newHashMap();
        model.put("username", mail.getContent());
        model.put("title", "标题Mail中使用了Thymeleaf");
        String templateName = "/mail/thymeleafMail.html";
        mailUtil.sendThymeleafTemplateMail(mail, model, templateName);
    }
}