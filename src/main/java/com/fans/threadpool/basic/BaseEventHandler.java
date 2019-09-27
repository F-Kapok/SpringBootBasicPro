package com.fans.threadpool.basic;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static com.fans.threadpool.basic.EventQueue.getThreadPoolExecutor;

/**
 * @ClassName EventHandler
 * @Description: 事件处理实现
 * @Author k
 * @Date 2019-08-16 16:58
 * @Version 1.0
 **/
@Slf4j
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
     * @return 容量大小 默认0
     */
    public int getCorePoolSize() {
        return 0;
    }

    /**
     * 设置线程池队列模式
     *
     * @return 队列类型 默认SynchronousQueue
     */
    public BlockingQueue<Runnable> getWorkQueue() {
        return new SynchronousQueue<>();
    }

    /**
     * 事件描述
     *
     * @return
     */
    public abstract String getDescription();

    /**
     * 汇总模式 子任务全部完成 才执行父任务
     *
     * @param obj        线程池key(eventBean)
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
