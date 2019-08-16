package com.fans.threadpool.basic;

import com.fans.threadpool.eventBean.MessageBean;
import com.fans.threadpool.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @ClassName PoolRegister
 * @Description: 事件队列注册中心
 * @Author k
 * @Date 2019-08-16 16:50
 * @Version 1.0
 **/
@Component
@Slf4j
public class PoolRegister {

    private static PoolRegister poolRegister;

    /**
     * 只注册一个执行事件的队列
     */
    public static EventQueue<MessageBean> sendMsgEventQueue;

    @Resource(name = "messageHandler")
    private MessageHandler messageHandler;

    @PostConstruct
    public void init() {
        log.info("--> PoolRegister init() @" + DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(DateTime.now()));
        poolRegister = this;
        poolRegister.messageHandler = this.messageHandler;
        initStatic();
    }

    private void initStatic() {
        sendMsgEventQueue = new EventQueue<>(messageHandler, 50);
    }
}
