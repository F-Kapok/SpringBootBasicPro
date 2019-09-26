package com.fans.threadpool.basic;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName EventQueue
 * @Description: 事件执行队列
 * @Author k
 * @Date 2019-08-16 16:51
 * @Version 1.0
 **/
@Slf4j
public class EventQueue<T> extends Observable {
    /**
     * 初始化线程池集合  key是bean.getName();
     */
    private static ImmutableMap.Builder<String, ThreadPoolExecutor> executorMapBuilder = ImmutableMap.builder();
    private static ImmutableMap<String, ThreadPoolExecutor> executorMap = ImmutableMap.<String, ThreadPoolExecutor>builder().build();
    /**
     * 事件队列
     */
    private final CopyOnWriteArrayList<T> queue = Lists.newCopyOnWriteArrayList();

    EventQueue(final BaseEventHandler<T> handler, int corePoolSize, Class<T> cls, BlockingQueue<Runnable> workQueue) {
        super();
        String beanName = cls.getName();
        String simpleName = cls.getSimpleName();
        String threadName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        if (!executorMap.containsKey(beanName)) {
            executorMapBuilder.put(beanName, new ThreadPoolExecutor(corePoolSize,
                    corePoolSize,
                    0L,
                    TimeUnit.MILLISECONDS,
                    workQueue,
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

    T take() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }

    static <T> ThreadPoolExecutor getThreadPoolExecutor(T obj) {
        return executorMap.get(obj.getClass().getName());
    }
}
