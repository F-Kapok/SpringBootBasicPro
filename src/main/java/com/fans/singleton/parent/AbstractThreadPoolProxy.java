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
     * @param task
     * @return 得到异步执行完成之后的结果
     * @des 提交任务
     */
    public abstract Future<?> submit(Runnable task);

    /**
     * @param task
     * @des 执行任务
     */
    public abstract void execute(Runnable task);

    /**
     * @param task
     * @des 移除任务
     */
    public abstract void remove(Runnable task);
}
