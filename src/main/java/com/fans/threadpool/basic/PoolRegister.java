package com.fans.threadpool.basic;

import com.fans.singleton.parent.AbstractLocalCacheProxy;
import com.fans.singleton.parent.AbstractThreadPoolProxy;
import com.fans.utils.JsonUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.reflections.Reflections;

import java.util.Set;

import static com.fans.utils.ReflectUtils.getMavenModel;

/**
 * @ClassName PoolRegister
 * @Description: 事件队列注册中心
 * @Author k
 * @Date 2019-08-16 16:50
 * @Version 1.0
 **/
@Slf4j
public class PoolRegister<T> {

    /**
     * 执行事件队列列表
     */
    private static ImmutableMap<String, EventQueue> eventQueueMap;
    /**
     * 线程队列名称列表
     */
    private static ImmutableMap<String, String> threadNameMap;

    static {
        ImmutableMap.Builder<String, EventQueue> eventQueueMapBuilder = ImmutableMap.builder();
        //线程池队列模式集合
        ImmutableMap.Builder<String, String> threadNameMapBuilder = ImmutableMap.builder();
        Set<Class<? extends BaseEventHandler>> threadPoolQueue = getThreadPoolQueue();
        //私有定制线程池集合
        ImmutableMap.Builder<String, String> privateThreadNameMapBuilder = ImmutableMap.builder();
        Set<Class<? extends AbstractThreadPoolProxy>> privateThreadPool = getPrivateThreadPool();
        //私有缓存池集合
        ImmutableMap.Builder<String, String> loadingCacheMapBuilder = ImmutableMap.builder();
        Set<Class<? extends AbstractLocalCacheProxy>> loadingCache = getloadingCache();
        threadPoolQueue.forEach(aClass -> {
            try {
                String typeName = aClass.getGenericSuperclass().getTypeName();
                String beanName = typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">"));
                Class cls = Class.forName(beanName);
                String simpleName = cls.getSimpleName();
                eventQueueMapBuilder.put(beanName, new EventQueue(aClass.newInstance(), 20, cls));
                threadNameMapBuilder.put(beanName, "The 【"
                        .concat(simpleName)
                        .concat("】 queue ready !!!  Action : ")
                        .concat(aClass.newInstance().getDescription())
                );
            } catch (Exception e) {
                log.error("-->  PoolRegister init() Fail", e);
                e.printStackTrace();
            }
        });
        eventQueueMap = eventQueueMapBuilder.build();
        threadNameMap = threadNameMapBuilder.build();
        //仅仅打印日志用
        privateThreadPool.forEach(aClass -> {
            try {
                String typeName = aClass.getGenericSuperclass().getTypeName();
                String beanName = typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">"));
                Class cls = Class.forName(beanName);
                String simpleName = cls.getSimpleName();
                Object instance = aClass.getDeclaredMethod("getInstance").invoke(null);
                privateThreadNameMapBuilder.put(beanName, "The ThreadPool 【"
                        .concat(simpleName)
                        .concat("】 init() @ success !!! Description : ")
                        .concat((String) aClass.getMethod("getDescription").invoke(instance)));
            } catch (Exception e) {
                log.error("-->  PoolRegister init() Fail", e);
                e.printStackTrace();
            }
        });
        loadingCache.forEach(aClass -> {
            try {
                String typeName = aClass.getGenericSuperclass().getTypeName();
                String beanName = typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">"));
                Class cls = Class.forName(beanName);
                String simpleName = cls.getSimpleName();
                Object instance = aClass.getDeclaredMethod("getInstance").invoke(null);
                loadingCacheMapBuilder.put(beanName, "The LoadingCache 【"
                        .concat(simpleName)
                        .concat("】 init() @ success !!! Description : ")
                        .concat((String) aClass.getMethod("getDescription").invoke(instance)));
            } catch (Exception e) {
                log.error("-->  PoolRegister init() Fail", e);
                e.printStackTrace();
            }
        });
        String threadPoolQueueDesc = JsonUtils.obj2FormattingString(threadNameMap.values());
        String privateThreadPoolDesc = JsonUtils.obj2FormattingString(privateThreadNameMapBuilder.build().values());
        String loadingCacheDesc = JsonUtils.obj2FormattingString(loadingCacheMapBuilder.build().values());
        log.info("\r\n--> PoolRegister init() @" + DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(DateTime.now()));
        String stringBuilder = "\r\n--> 可用线程队列列表(poolRegister.executeHandler(eventBean)) : \r\n{}" +
                "\r\n--> 私有线程池列表(ThreadPoolProxyFactory.getThreadPoolProxy()) : \r\n{}" +
                "\r\n--> 私有缓存池列表(LocalCacheProxyFactory.getLocalCacheProxy()) : \r\n{}";
        log.info(stringBuilder, threadPoolQueueDesc, privateThreadPoolDesc, loadingCacheDesc);
    }

    private static Set<Class<? extends BaseEventHandler>> getThreadPoolQueue() {
        Reflections reflections = getReflections();
        return reflections.getSubTypesOf(BaseEventHandler.class);
    }

    private static Set<Class<? extends AbstractThreadPoolProxy>> getPrivateThreadPool() {
        Reflections reflections = getReflections();
        return reflections.getSubTypesOf(AbstractThreadPoolProxy.class);
    }

    private static Set<Class<? extends AbstractLocalCacheProxy>> getloadingCache() {
        Reflections reflections = getReflections();
        return reflections.getSubTypesOf(AbstractLocalCacheProxy.class);
    }

    private static Reflections getReflections() {
        Reflections reflections = null;
        try {
            Model mavenModel = getMavenModel();
            String groupId = mavenModel.getGroupId();
            reflections = new Reflections(groupId);
        } catch (Exception e) {
            log.error("-->  PoolRegister init() Fail", e);
            e.printStackTrace();
        }
        assert reflections != null;
        return reflections;
    }

    public void executeHandler(T paramBean) {
        Class cls = paramBean.getClass();
        String beanName = cls.getName();
        EventQueue eventQueue = eventQueueMap.get(beanName);
        eventQueue.add(paramBean);
    }

    public void peek(T paramBean) {
        String beanName = paramBean.getClass().getName();
        if (threadNameMap.containsKey(beanName)) {
            System.out.println("线程队列详情 : \r\n" + JsonUtils.obj2FormattingString(Lists.newArrayList(threadNameMap.get(beanName))));
        } else {
            System.out.println(JsonUtils.obj2FormattingString(Lists.newArrayList()));
        }

    }
}
