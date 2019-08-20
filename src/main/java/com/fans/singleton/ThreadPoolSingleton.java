package com.fans.singleton;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @ClassName ThreadPoolSingleton
 * @Description: 线程池单例初始化---根据需求定制
 * @Author k
 * @Date 2019-08-16 15:02
 * @Version 1.0
 **/
public class ThreadPoolSingleton {
    /**
     * 线程池基本大小，大于此值会开启新的线程执行至maximumPoolSize
     */
    private int corePoolSize = 50;
    /**
     * 线程池最大处理线程数
     */
    private int maximumPoolSize = Integer.MAX_VALUE;
    /**
     * 线程池线程空闲后停留时间
     */
    private long keepAliveTime = 60L;
    /**
     * 线程数停留时间单位
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    /**
     * LinkedBlockingDeque:是一个基于链表结构的有界阻塞队列，此队列按照FIFO排序元素，吞吐量高于ArrayBlockingQueue。静态工厂方法Executors.newFixedThreadPool(n)使用了此队列
     * LinkedTransferQueue
     * ArrayBlockingQueue:是一个基于数组结构的有界阻塞队列，此队列按照FIFO（先进先出）原则对元素进行排序
     * DelayQueue
     * PriorityBlockingQueue:一个具有优先级的无限阻塞队列
     * SynchronousQueue:一个不存储元素的阻塞队列。每个插入操作必须等待另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量要高于LinkedBlockingQueue，静态工厂方法Executors.newCachedThreadPool()使用了此队列
     * 任务队列。用来暂时保存任务的工作队列
     */
    private LinkedBlockingDeque workQueue = new LinkedBlockingDeque();
    /**
     * 线程工厂构建基本信息
     */
    private ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("thread-task-runner-%d")
            .build();
    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue, threadFactory);

    private ThreadPoolSingleton() {
    }

    public static ThreadPoolSingleton getInstance() {
        return Instance.INSTANCE.getThreadPool();
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPoolExecutor;
    }

    private enum Instance {
        /**
         * 初始化线程池
         */
        INSTANCE;

        private ThreadPoolSingleton threadPool;

        Instance() {
            this.threadPool = new ThreadPoolSingleton();
        }

        public ThreadPoolSingleton getThreadPool() {
            return threadPool;
        }
    }
}
