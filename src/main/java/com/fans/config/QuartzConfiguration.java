package com.fans.config;

import com.fans.quartz.SchedulerOperate;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @ClassName QuartzConfiguration
 * @Description: 定时任务配置
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
        //可选,QuartzScheduler启动时更新己存在的Job,
        // 这样就不用每次修改targetObject后删除qrtz_job_details表对应记录
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
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/properties/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
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
    public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements
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
