package com.fans.config;

import com.fans.quartz.SchedulerOperate;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @ClassName QuartzConfiguration
 * @Description: 定时任务配置
 * https://www.cnblogs.com/interdrp/p/5003257.html Cron表达式
 * @Author fan
 * @Date 2018-12-22 13:57
 * @Version 1.0
 **/
@Configuration
@EnableScheduling
public class QuartzConfiguration {
    /**
     * @Description: 配置JobFactory
     * @Param: [applicationContext]
     * @return: org.quartz.spi.JobFactory
     * @Author: fan
     * @Date: 2018/12/22 14:00
     **/
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * @Description: SchedulerFactoryBean这个类的真正作用提供了对org.quartz.Scheduler的创建与配置，
     * @Description: 并且会管理它的生命周期与Spring同步。
     * @Description: org.quartz.Scheduler: 调度器。所有的调度都是由它控制。
     * @Param: [dataSource(为SchedulerFactory配置数据源), jobFactory(为SchedulerFactory配置JobFactory)]
     * @return: org.springframework.scheduling.quartz.SchedulerFactoryBean
     * @Author: fan
     * @Date: 2018/12/22 14:04
     **/
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName("Cream-K-Scheduler");
        //延时启动
        factory.setStartupDelay(30);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        //可选,QuartzScheduler启动时更新己存在的Job, 这样就不用每次修改targetObject后删除qrtz_job_details表对应记录
        factory.setOverwriteExistingJobs(true);
        //设置自行启动
        factory.setAutoStartup(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    /**
     * @Description: 从quartz.properties文件中读取Quartz配置属性
     * @Param: []
     * @return: java.util.Properties
     * @Author: fan
     * @Date: 2018/12/22 14:01
     **/
    @Bean
    public Properties quartzProperties() {
        //quartz参数
        Properties prop = new Properties();
        //使用自己的配置文件
        prop.put("org.quartz.jobStore.useProperties", "true");
        //默认或是自己改名字都行
        prop.put("org.quartz.scheduler.instanceName", "Cream-K-QuartzScheduler");
        //如果使用集群，instanceId必须唯一，设置成AUTO
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        //线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "10");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
        //存储方式使用JobStoreTX，也就是数据库
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        //集群配置 是否使用集群（如果项目只部署到 一台服务器，就不用了）
        prop.put("org.quartz.jobStore.isClustered", "true");
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "20000");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        //容许的最大作业延长时间
        prop.put("org.quartz.jobStore.misfireThreshold", "25000");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        prop.put("org.quartz.jobStore.dataSource", "myDS");
        prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE LOCK_NAME = ?");
        //指定调度程序的主线程是否应该是守护线程
        prop.put("org.quartz.scheduler.makeSchedulerThreadDaemon", "true");
        //ThreadPool配置线程守护进程
        prop.put("org.quartz.threadPool.makeThreadsDaemons", "true");
        //PostgreSQL数据库，需要打开此注释
        //prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        return prop;
    }

    @Bean
    public SchedulerOperate schedulerOperate() {
        return new SchedulerOperate();
    }

    /**
     * @Description: 配置JobFactory, 为quartz作业添加自动连接支持
     * @Param:
     * @return:
     * @Author: fan
     * @Date: 2018/12/22 13:59
     **/
    public static final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements
            ApplicationContextAware {
        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }
}
