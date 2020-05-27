package com.fans.quartz;

import com.fans.pojo.JobEntity;
import com.fans.service.interfaces.IDynamicJobService;
import com.fans.utils.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;
import java.util.Set;

/**
 * className: SchedulerOperate
 *
 * @author k
 * @version 1.0
 * @description 调度中心操作类
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class SchedulerOperate {

    /*
     * 初始化所有Job
     */
    static {
        try {
            restartAllJobs();
            log.info("quartz--> All Job init() @ Success");
        } catch (Exception e) {
            log.error("quartz--> All Job Init Error : {}", e.getMessage());
        }
    }

    /**
     * description: 重启所有Job
     *
     * @author k
     * @date 2018/12/22 16:15
     **/
    public static void restartAllJobs() {
        SchedulerFactoryBean factory = ApplicationContextHelper.popBean(SchedulerFactoryBean.class);
        IDynamicJobService dynamicJobService = ApplicationContextHelper.popBean("iDynamicJobService", IDynamicJobService.class);
        assert factory != null;
        Scheduler scheduler = factory.getScheduler();
        try {
            Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
            for (JobKey jobKey : jobKeySet) {
                scheduler.deleteJob(jobKey);
            }
            assert dynamicJobService != null;
            List<JobEntity> jobEntities = dynamicJobService.loadJobs();
            for (JobEntity entity : jobEntities) {
                log.info("quartz--> Job Register name : {} , group : {} , cron : {} ", entity.getName(), entity.getGroup(), entity.getCron());
                JobDataMap jobDataMap = dynamicJobService.getJobDataMap(entity);
                JobKey jobKey = dynamicJobService.getJobKey(entity);
                JobDetail jobDetail = dynamicJobService.getJobDetail(jobKey, entity.getDescription(), jobDataMap);
                if (StringUtils.equals(entity.getStatus(), "OPEN")) {
                    scheduler.scheduleJob(jobDetail, dynamicJobService.getTrigger(entity));
                } else {
                    log.info("quartz--> Job jump name : {} , group : {} , Because {} status is {}", entity.getName(), entity.getGroup(), entity.getName(), entity.getStatus());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
