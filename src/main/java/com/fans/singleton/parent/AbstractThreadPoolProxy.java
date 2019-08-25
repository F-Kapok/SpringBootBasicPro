package com.fans.singleton.parent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;

/**
 * @ClassName ThreadPoolProxy
 * @Description:
 * @Author k
 * @Date 2019-08-25 04:35
 * @Version 1.0
 **/
@Slf4j
public abstract class AbstractThreadPoolProxy {


    /**
     * 提交任务
     *
     * @param task
     * @return 得到异步执行完成之后的结果
     */
    public abstract Future<?> submit(Runnable task);

    /**
     * 执行任务
     *
     * @param task
     */
    public abstract void execute(Runnable task);

    /**
     * 移除任务
     *
     * @param task
     */
    public abstract void remove(Runnable task);
}
