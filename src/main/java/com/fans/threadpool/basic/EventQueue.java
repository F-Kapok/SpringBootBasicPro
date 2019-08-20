package com.fans.threadpool.basic;

import com.fans.singleton.ThreadPoolSingleton;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Observable;
import java.util.Vector;
import java.util.concurrent.*;

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

    EventQueue(final EventHandler<T> handler, int corePoolSize, String threadName) {
        super();
        //获取单例线程池
        executor = new ThreadPoolExecutor(corePoolSize,
                corePoolSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder()
                        .setNameFormat(threadName.concat("-thread-task-runner-%d"))
                        .build());
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
