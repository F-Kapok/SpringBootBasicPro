package com.fans.threadpool.handler;

import com.fans.threadpool.basic.EventHandler;
import com.fans.threadpool.eventBean.MessageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName MessageHandler
 * @Description: 消息执行器  增加 Component 是为了引用ioc管理的容器和注册中心使用
 * @Author k
 * @Date 2019-08-16 17:34
 * @Version 1.0
 **/
@Component("messageHandler")
@Slf4j
public class MessageHandler extends EventHandler<MessageBean> {


    @Override
    public void execute(MessageBean event) {
        log.info("--->>>> 短信执行器触发，已发送成功！！！");
        log.info("--> 你好" + event.getName() + "，我今年" + event.getAge() + "岁");

    }
}
