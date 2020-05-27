package com.fans.singleton.proxy;

import com.fans.singleton.parent.AbstractThreadPoolProxy;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * className: ThreadPoolProxy
 *
 * @author k
 * @version 1.0
 * @description 线程池单例初始化---由工厂生成
 * @date 2018-12-20 14:14
 **/
public class ThreadPoolProxy extends AbstractThreadPoolProxy<ThreadPoolProxy> {
    /**
     * 线程池基本大小，大于此值会开启新的线程执行至maximumPoolSize
     */
    private final int corePoolSize = 0;
    /**
     * 线程池最大处理线程数
     */
    private final int maximumPoolSize = Integer.MAX_VALUE;
    /**
     * 线程池线程空闲后停留时间
     */
    private final long keepAliveTime = 60L;
    /**
     * 线程数停留时间单位
     */
    private final TimeUnit timeUnit = TimeUnit.SECONDS;
    /**
     * ArrayBlockingQueue ：一个由数组结构组成的有界阻塞队列。 照FIFO（先进先出）原则对元素进行排序
     * <p>
     * LinkedBlockingQueue ：一个由链表结构组成的有界阻塞队列。
     * 按照FIFO（先进先出）原则对元素进行排序 吞吐量高于ArrayBlockingQueue。
     * 静态工厂方法Executors.newFixedThreadPool(n)使用了此队列
     * <p>
     * PriorityBlockingQueue ：一个支持优先级排序的无界阻塞队列。
     * <p>
     * DelayQueue： 一个使用优先级队列实现的无界阻塞队列。
     * <p>
     * SynchronousQueue： 一个不存储元素的阻塞队列。每个插入操作必须等待另一个线程调用移除操作，
     * 否则插入操作一直处于阻塞状态，吞吐量要高于LinkedBlockingQueue，
     * 静态工厂方法Executors.newCachedThreadPool()使用了此队列
     * 任务队列。用来暂时保存任务的工作队列
     * <p>
     * LinkedTransferQueue： 一个由链表结构组成的无界阻塞队列。
     * <p>
     * LinkedBlockingDeque： 一个由链表结构组成的双向阻塞队列。
     */
    private final SynchronousQueue workQueue = new SynchronousQueue();

    /**
     * ThreadPoolExecutor.AbortPolicy: 丢弃任务并抛出
     * RejectedExecutionException异常。 (默认)
     * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
     * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
     */
    private final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
    /**
     * 线程工厂构建基本信息
     */
    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("thread-task-runner-%d")
            .build();
    /**
     * 线程池
     */
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue, threadFactory, rejectedExecutionHandler);

    private ThreadPoolProxy() {
    }

    public static ThreadPoolProxy getInstance() {
        return Instance.INSTANCE.getThreadPool();
    }

    private ThreadPoolExecutor getThreadPool() {
        return threadPoolExecutor;
    }

    @Override
    public String getDescription() {
        return "本地线程池";
    }

    @Override
    public void execute(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    @Override
    public void remove(Runnable task) {
        threadPoolExecutor.remove(task);
    }

    private enum Instance {
        /**
         * 初始化线程池
         */
        INSTANCE;

        private final ThreadPoolProxy threadPool;

        Instance() {
            this.threadPool = new ThreadPoolProxy();
        }

        public ThreadPoolProxy getThreadPool() {
            return threadPool;
        }
    }
}
