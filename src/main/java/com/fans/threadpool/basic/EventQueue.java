package com.fans.threadpool.basic;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Observable;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName EventQueue
 * @Description: 事件执行队列
 * @Author k
 * @Date 2019-08-16 16:51
 * @Version 1.0
 **/
public class EventQueue<T> extends Observable {
    /**
     * 初始化线程池集合  key是bean.getName();
     */
    private static ImmutableMap.Builder<String, ThreadPoolExecutor> executorMapBuilder = ImmutableMap.builder();
    private static ImmutableMap<String, ThreadPoolExecutor> executorMap = ImmutableMap.<String, ThreadPoolExecutor>builder().build();
    /**
     * 事件队列
     */
    private final Vector<T> queue = new Vector<>();

    public EventQueue(final BaseEventHandler<T> handler, int corePoolSize, Class<T> cls) {
        super();
        String beanName = cls.getName();
        String simpleName = cls.getSimpleName();
        String threadName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        if (!executorMap.containsKey(beanName)) {
            executorMapBuilder.put(beanName, new ThreadPoolExecutor(corePoolSize,
                    corePoolSize,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new ThreadFactoryBuilder()
                            .setNameFormat(threadName.concat("-thread-task-runner-%d"))
                            .build(), new ThreadPoolExecutor.AbortPolicy()));
            executorMap = executorMapBuilder.build();
        }
        handler.setQueue(this);
        this.addObserver((observable, object) -> executorMap.get(beanName).execute(handler));
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

    public static <T> ThreadPoolExecutor getThreadPoolExecutor(T obj) {
        return executorMap.get(obj.getClass().getName());
    }
}
