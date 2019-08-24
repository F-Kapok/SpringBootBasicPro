package com.fans.threadpool.basic;

import com.fans.utils.JsonUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

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
        ImmutableMap.Builder<String, String> threadNameMapBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, EventQueue> eventQueueMapBuilder = ImmutableMap.builder();
        Set<Class<? extends BaseEventHandler>> classSet = getClassSet();
        classSet.forEach(aClass -> {
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
        log.info("--> PoolRegister init() @" + DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(DateTime.now()));
        log.info("--> 可用线程队列列表 : \r\n{}", JsonUtils.obj2FormattingString(threadNameMap.values()));
    }

    private static Set<Class<? extends BaseEventHandler>> getClassSet() {
        Reflections reflections = null;
        try {
            File file = new File("pom.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            Model mavenModel = mavenXpp3Reader.read(fileInputStream);
            String groupId = mavenModel.getGroupId();
            reflections = new Reflections(groupId);
        } catch (Exception e) {
            log.error("-->  PoolRegister init() Fail", e);
            e.printStackTrace();
        }
        assert reflections != null;
        return reflections.getSubTypesOf(BaseEventHandler.class);
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
