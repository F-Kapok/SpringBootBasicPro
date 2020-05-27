package com.fans.utils;

import com.fans.common.Mail;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.Objects;


/**
 * className: MailUtils
 *
 * @author k
 * @version 1.0
 * @description 邮件发送工具类
 * @date 2018-12-20 14:14
 **/
@Component(value = "mailUtils")
@ConfigurationProperties(prefix = "spring.mail")
@Data
@Slf4j
public class MailUtils {
    /**
     * 邮件发送者
     */
    private String mailSender;

    @Resource(type = JavaMailSender.class)
    private JavaMailSender javaMailSender;
    /**
     * freemarker
     */
    @Resource(type = Configuration.class)
    private Configuration configuration;
    /**
     * thymeleaf
     */
    @Resource(name = "templateEngine")
    private TemplateEngine templateEngine;

    /**
     * description: 发送一个简单格式的邮件
     *
     * @param mail 邮件
     * @author k
     * @date 2019/03/18 17:51
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
            log.error("--> 邮件发送失败,失败原因:{}", e.getMessage());

        }
    }

    /**
     * description: 发送一个HTML格式的邮件
     *
     * @param mail 邮箱
     * @author k
     * @date 2020/05/27 22:47
     **/
    public void sendHTMLMail(Mail mail) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            mimeMessageHelper.setText(mail.getContent(), true);
            javaMailSender.send(mimeMailMessage);
            log.info("--> 发送成功！！！");
        } catch (Exception e) {
            log.error("--> 邮件发送失败,失败原因:{}", e.getMessage());
        }
    }

    /**
     * @Description: 发送带附件格式的邮件
     * @Param: [mail]
     * @return: void
     * @Author: fan
     * @Date: 2019/03/18 17:51
     **/
    public void sendAttachmentMail(Mail mail, String pathName) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            mimeMessageHelper.setText(mail.getContent());
            FileSystemResource fileSystemResource = new FileSystemResource(new File(pathName));
            mimeMessageHelper.addAttachment(Objects.requireNonNull(fileSystemResource.getFilename()), fileSystemResource);
            javaMailSender.send(mimeMailMessage);
            log.info("--> 发送成功！！！");
        } catch (Exception e) {
            log.error("--> 邮件发送失败,失败原因:{}", e.getMessage());
        }
    }

    /**
     * description: 发送带静态资源的邮件
     *
     * @param mail      邮件
     * @param contentId 内容id
     * @param pathName  路径名
     * @author k
     * @date 2020/05/27 23:00
     **/
    public void sendInlineMail(Mail mail, String contentId, String pathName) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            mimeMessageHelper.setText(mail.getContent(), true);
            FileSystemResource file = new FileSystemResource(new File(pathName));
            mimeMessageHelper.addInline(contentId, file);
            javaMailSender.send(mimeMailMessage);
            log.info("--> 发送成功！！！");
        } catch (Exception e) {
            log.error("--> 邮件发送失败,失败原因:{}", e.getMessage());
        }
    }

    /**
     * description: 发送基于Freemarker模板的邮件
     *
     * @param mail         邮件
     * @param inParam      内容体
     * @param templateName 模板名
     * @author k
     * @date 2019/03/18 17:52
     **/
    public void sendFreemarkerTemplateMail(Mail mail, Map<String, Object> inParam, String templateName) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            Template template = configuration.getTemplate(templateName);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, inParam);
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMailMessage);
            log.info("--> 发送成功！！！");
        } catch (Exception e) {
            log.error("--> 邮件发送失败,失败原因:{}", e.getMessage());
        }

    }

    public void sendThymeleafTemplateMail(Mail mail, Map<String, Object> inParam, String templateName) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(mimeMailMessage, mail);
            // context 对象用于注入要在模板上渲染的信息
            Context context = new Context();
            context.setVariables(inParam);
            String text = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMailMessage);
            log.info("--> 发送成功！！！");
        } catch (Exception e) {
            log.error("--> 邮件发送失败,失败原因:{}", e.getMessage());
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
