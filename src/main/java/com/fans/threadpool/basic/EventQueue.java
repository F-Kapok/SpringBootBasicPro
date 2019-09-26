package com.fans.threadpool.basic;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Observable;
import java.util.concurrent.*;

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

    private static <T> ThreadPoolExecutor getThreadPoolExecutor(T obj) {
        return executorMap.get(obj.getClass().getName());
    }

    /**
     * 汇总模式 子任务全部完成 才执行父任务
     *
     * @param obj        线程池key
     * @param time       超时时间
     * @param timeUnit   时间单位
     * @param parentTask 父任务
     * @param childTask  子任务
     * @param <T>        线程池key对象
     */
    public static <T> void gatherSubmit(T obj, long time, TimeUnit timeUnit, Callable<Boolean> parentTask, Callable<Boolean>... childTask) {
        ThreadPoolExecutor executor = getThreadPoolExecutor(obj);
        Future<Boolean> future;
        String callableName = "";
        try {
            ImmutableSet.Builder<Boolean> builder = ImmutableSet.builder();
            ImmutableSet<Boolean> set;

            for (Callable<Boolean> callable : childTask) {
                callableName = callable.getClass().getSimpleName();
                future = executor.submit(callable);
                builder.add(future.get(time, timeUnit));
            }
            set = builder.build();
            if (set.size() == 1 && set.contains(true)) {
                executor.submit(parentTask);
            }
        } catch (Exception e) {
            String name = e.getClass().getName();
            if (name.equals(TimeoutException.class.getName())) {
                log.error("--> gatherSubmit : callable {} have error {}", callableName, name);
                try {
                    throw new Exception("--> gatherSubmit : callable {} have error {}");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                log.error("--> gatherSubmit : callable {} have error", callableName, e);
            }
        }


    }

}
