package com.fans.singleton.parent;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * className: ThreadPoolProxy
 *
 * @author k
 * @version 1.0
 * @description 控制层AOP
 * @date 2018-12-20 14:14
 **/
@Slf4j
public abstract class AbstractThreadPoolProxy<T> {

    private Class<T> aClass;

    /**
     * 线程池描述
     *
     * @return 描述
     */
    public abstract String getDescription();

    /**
     * 提交任务
     *
     * @param task 任务
     * @return 得到异步执行完成之后的结果
     */
    public Future<?> submit(Runnable task) {
        try {
            //获取泛型类型数组
            ThreadPoolExecutor threadPool = getThreadPoolExecutor();
            assert threadPool != null;
            if (!threadPool.isShutdown()) {
                return threadPool.submit(task);
            } else {
                log.error("--> {} is shutdown", aClass.getSimpleName());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        ThreadPoolExecutor threadPool = getThreadPoolExecutor();
        assert threadPool != null;
        if (!threadPool.isShutdown()) {
            threadPool.execute(task);
        } else {
            log.error("--> {} is shutdown", aClass.getSimpleName());
        }

    }

    /**
     * 移除任务
     *
     * @param task 任务
     */
    public void remove(Runnable task) {
        ThreadPoolExecutor threadPool = getThreadPoolExecutor();
        assert threadPool != null;
        if (!threadPool.isShutdown()) {
            threadPool.remove(task);
        } else {
            log.error("--> {} is shutdown", aClass.getSimpleName());
        }
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        ThreadPoolExecutor threadPool = getThreadPoolExecutor();
        assert threadPool != null;
        threadPool.shutdown();
    }

    /**
     * 反射获取泛型类型
     *
     * @return java.util.concurrent.ThreadPoolExecutor
     */
    private ThreadPoolExecutor getThreadPoolExecutor() {
        try {
            //获取泛型类型数组
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            //获取泛型类型
            aClass = (Class<T>) pt.getActualTypeArguments()[0];
            Method instance = aClass.getDeclaredMethod("getInstance");
            Method method = aClass.getDeclaredMethod("getThreadPool");
            method.setAccessible(true);
            return (ThreadPoolExecutor) method.invoke(instance.invoke(null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
