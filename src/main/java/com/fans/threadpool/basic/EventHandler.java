package com.fans.threadpool.basic;

/**
 * @ClassName EventHandler
 * @Description: 事件处理实现
 * @Author k
 * @Date 2019-08-16 16:58
 * @Version 1.0
 **/
public abstract class EventHandler<T> implements Runnable {

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


}
