package com.fans.wechat.robot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fans.wechat.robot.param.*;
import com.fans.wechat.robot.vo.MessageResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * className: RobotMessageUtils
 *
 * @author k
 * @version 1.0
 * @description 机器人消息推送工具类
 * @date 2019-04-13 09:43
 **/
@Slf4j
public class RobotMessageUtils {

    private static final String URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key={0}";


    public static <T extends BodyBase> MessageResult sendMessage(RobotEnum robot, T body) throws Exception {
        JSONObject param = new JSONObject();
        MessageResult result;
        if (body instanceof TextBody) {
            if (StringUtils.isBlank(((TextBody) body).getContent())) {
                throw new RuntimeException("消息不能为空!!!");
            }
            param.put(BodyBase.KEY, BodyBase.TEXT);
            param.put(BodyBase.TEXT, body);
        } else if (body instanceof MarkdownBody) {
            if (StringUtils.isBlank(((MarkdownBody) body).getContent())) {
                throw new RuntimeException("消息不能为空!!!");
            }
            param.put(BodyBase.KEY, BodyBase.MARKDOWN);
            param.put(BodyBase.MARKDOWN, body);
        } else if (body instanceof ImageBody) {
            if (StringUtils.isBlank(((ImageBody) body).getBase64())) {
                throw new RuntimeException("图片base64不能为空!!!");
            }
            if (StringUtils.isBlank(((ImageBody) body).getMd5())) {
                throw new RuntimeException("图片md5不能为空!!!");
            }
            param.put(BodyBase.KEY, BodyBase.IMAGE);
            param.put(BodyBase.IMAGE, body);
        } else if (body instanceof NewsBody) {
            if (((NewsBody) body).getArticles().isEmpty()) {
                throw new RuntimeException("图文消息体不能为空!!!");
            } else {
                AtomicInteger i = new AtomicInteger(0);
                ((NewsBody) body).getArticles().forEach(newBody -> {
                    i.incrementAndGet();
                    if (StringUtils.isBlank(newBody.getTitle())) {
                        throw new RuntimeException("第" + i.get() + "条 图文消息体标题不能为空!!!");
                    }
                    if (StringUtils.isBlank(newBody.getUrl())) {
                        throw new RuntimeException("第" + i.get() + "条 图文消息体链接不能为空!!!");
                    }
                });
            }
            param.put(BodyBase.KEY, BodyBase.NEWS);
            param.put(BodyBase.NEWS, body);
        } else if (body instanceof FileBody) {
            if (StringUtils.isBlank(((FileBody) body).getMedia_id())) {
                throw new RuntimeException("文件不能为空!!!");
            }
            param.put(BodyBase.KEY, BodyBase.FILE);
            param.put(BodyBase.FILE, body);
        }
        result = send(robot, param);
        return result;
    }


    private static MessageResult send(RobotEnum robot, JSONObject param) throws Exception {
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        String resultString;
        try {
            HttpPost httpPost = new HttpPost(MessageFormat.format(RobotMessageUtils.URL, robot.getKey()));
            StringEntity entity = new StringEntity(param.toJSONString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            response = closeableHttpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return JSON.parseObject(resultString, MessageResult.class);
    }

    public static void main(String[] args) {
        try {
            //文本类型示例
            MessageResult textResult = RobotMessageUtils.sendMessage(RobotEnum.ROBOT_ONE, TextBody.builder()
                    .content("你个小坏蛋")
                    .mentioned_list(Lists.newArrayList("@all"))
                    .build());
            log.info("--> result:{}", JSON.toJSONString(textResult, true));
            //********************************************************************************************
            //markdown本类型示例
            MessageResult markdownResult = RobotMessageUtils.sendMessage(RobotEnum.ROBOT_ONE, MarkdownBody.builder()
                    .content(
                            "### 实时新增用户反馈<font color=\"warning\">132例</font>，请相关同事注意。" +
                                    "\n>类型:<font color=\"comment\" > 用户反馈</font> " +
                                    "\n>普通用户反馈:<font color=\"comment\">117例</font> " +
                                    "\n>VIP用户反馈:<font color=\"info\">15例</font>" +
                                    "\n `System.out.println(\"代码块不支持换行\");`" +
                                    "\n[详情](https://www.baidu.com)"
                    )
                    .build());
            log.info("--> result:{}", JSON.toJSONString(markdownResult, true));
            //********************************************************************************************
            //图片类型示例
            MessageResult imageResult = RobotMessageUtils.sendMessage(RobotEnum.ROBOT_ONE, ImageBody.builder()
                    .base64(ImageBody.getBase64("C:\\Users\\Administrator\\Pictures\\background.jpg"))
                    .md5(ImageBody.getMd5("C:\\Users\\Administrator\\Pictures\\background.jpg"))
                    .build());
            log.info("--> result:{}", JSON.toJSONString(imageResult, true));
            //********************************************************************************************
            //图文类型示例
            MessageResult newsResult = RobotMessageUtils.sendMessage(RobotEnum.ROBOT_ONE, NewsBody.builder()
                    .articles(Lists.newArrayList(
                            NewBody.builder()
                                    .title("中秋节礼品领取")
                                    .description("今年中秋节公司有豪礼相送")
                                    .url("www.qq.com")
                                    .picurl("http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png")
                                    .build()
                    ))
                    .build());
            log.info("--> result:{}", JSON.toJSONString(newsResult, true));
            //********************************************************************************************
            //文件发送示例
            MessageResult fileResult = RobotMessageUtils.sendMessage(RobotEnum.ROBOT_ONE, FileBody.builder()
                    .media_id(FileBody.getMediaId(RobotEnum.ROBOT_ONE, "C:\\Users\\Administrator\\Desktop\\wechat-shop.pdm"))
                    .build());
            log.info("--> result:{}", JSON.toJSONString(fileResult, true));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

