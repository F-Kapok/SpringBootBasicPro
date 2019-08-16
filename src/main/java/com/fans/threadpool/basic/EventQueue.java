package com.fans.threadpool.basic;

import com.fans.utils.ThreadPoolSingleton;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName EventQueue
 * @Description: 事件执行队列
 * @Author k
 * @Date 2019-08-16 16:51
 * @Version 1.0
 **/
public class EventQueue<T> extends Observable {
    /**
     * 初始化线程池
     */
    private static ThreadPoolExecutor executor;
    /**
     * 事件队列
     */
    private final Vector<T> queue = new Vector<>();

    EventQueue(final EventHandler<T> handler, int corePoolSize) {
        super();
        //获取单例线程池
        executor = ThreadPoolSingleton.getInstance().getThreadPool(corePoolSize);
        handler.setQueue(this);
        this.addObserver((observable, object) -> executor.execute(handler));
    }

    public void add(T event) {
        setChanged();
        synchronized (queue) {
            queue.add(event);
        }
        notifyObservers();
    }

    public T take() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }
}
