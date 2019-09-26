package com.fans.threadpool.basic;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName EventHandler
 * @Description: 事件处理实现
 * @Author k
 * @Date 2019-08-16 16:58
 * @Version 1.0
 **/
public abstract class BaseEventHandler<T> implements Runnable {

    private EventQueue<T> queue;

    @Override
    public void run() {
        T event;
        synchronized (queue) {
            event = queue.take();
        }
        if (event != null) {
            execute(event);
        }
    }

    public void setQueue(EventQueue<T> queue) {
        this.queue = queue;
    }

    /**
     * 执行事件
     *
     * @param event
     */
    public abstract void execute(T event);

    /**
     * 设置事件执行线程池容量
     *
     * @return 容量大小 默认20
     */
    public int getCorePoolSize() {
        return 20;
    }

    /**
     * 设置线程池队列模式
     *
     * @return 队列类型 默认LinkedBlockingQueue
     */
    public BlockingQueue<Runnable> getWorkQueue() {
        return new LinkedBlockingQueue<>();
    }

    /**
     * 事件描述
     *
     * @return
     */
    public abstract String getDescription();


}
