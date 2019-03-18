package com.fans.utils;

import com.fans.common.Mail;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;


/**
 * @ClassName MailUtil
 * @Description: 邮件发送工具类
 * @Author fan
 * @Date 2019-03-18 17:52
 * @Version 1.0
 **/
@Component(value = "mailUtil")
@Slf4j
public class MailUtil {
    /**
     * 邮件发送者
     */
    @Value("${spring.mail.sender}")
    private String mailSender;

    @Resource(type = JavaMailSender.class)
    private JavaMailSender javaMailSender;
    /**
     * freemarker
     */
    @Resource(type = Configuration.class)
    private Configuration configuration;


    /**
     * @Description: 发送一个简单格式的邮件
     * @Param: [mail]
     * @return: void
     * @Author: fan
     * @Date: 2019/03/18 17:51
     **/
    public void sendSimpleMail(Mail mail) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            //邮件发送人
            simpleMailMessage.setFrom(mailSender);
            //邮件接收人
            simpleMailMessage.setTo(mail.getRecipient());
            //邮件主题
            simpleMailMessage.setSubject(mail.getSubject());
            //邮件内容
            simpleMailMessage.setText(mail.getContent());
            javaMailSender.send(simpleMailMessage);
            log.info("--> 发送成功！！！");
        } catch (Exception e) {
            log.error("--> 邮件发送失败", e.getMessage());
        }
    }

    /**
     * @Description: 发送一个HTML格式的邮件
     * @Param: [mail]
     * @return: void
     * @Author: fan
     * @Date: 2019/03/18 17:51
     **/
    public void sendHTMLMail(Mail mail) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            String sb = "<h1>Springboot测试邮件HTML</h1>" +
                    "\"<p style='color:#F00'>邮件测试用例</p>" +
                    "<p style='text-align:right'>右对齐</p>";
            mimeMessageHelper.setText(sb, true);
            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            log.error("--> 邮件发送失败", e.getMessage());
        }
    }

    /**
     * @Description: 发送带附件格式的邮件
     * @Param: [mail]
     * @return: void
     * @Author: fan
     * @Date: 2019/03/18 17:51
     **/
    public void sendAttachmentMail(Mail mail) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            mimeMessageHelper.setText(mail.getContent());
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/kapok.jpg"));
            mimeMessageHelper.addAttachment("kapok.jpg", file);
            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            log.error("--> 邮件发送失败", e.getMessage());
        }
    }

    /**
     * @Description: 发送带静态资源的邮件
     * @Param: [mail]
     * @return: void
     * @Author: fan
     * @Date: 2019/03/18 17:52
     **/
    public void sendInlineMail(Mail mail) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            mimeMessageHelper.setText("<html><body>带静态资源的邮件内容，这个一张IDEA配置的照片:<img src='cid:picture' /></body></html>", true);
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/mail.png"));
            mimeMessageHelper.addInline("picture", file);

            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            log.error("邮件发送失败", e.getMessage());
        }
    }

    /**
     * @Description: 发送基于Freemarker模板的邮件
     * @Param: [mail]
     * @return: void
     * @Author: fan
     * @Date: 2019/03/18 17:52
     **/
    public void sendTemplateMail(Mail mail) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            Map<String, Object> model = Maps.newHashMap();
            model.put("content", mail.getContent());
            model.put("title", "标题Mail中使用了FreeMarker");
            Template template = configuration.getTemplate("mail.ftl");
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            log.error("--> 邮件发送失败", e.getMessage());
        }

    }

    private MimeMessageHelper getMimeMessageHelper(MimeMessage mimeMailMessage, Mail mail) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
        mimeMessageHelper.setFrom(mailSender);
        mimeMessageHelper.setTo(mail.getRecipient());
        mimeMessageHelper.setSubject(mail.getSubject());
        return mimeMessageHelper;
    }

}
