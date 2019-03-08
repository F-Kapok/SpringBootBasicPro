package com.fans.controller;

import com.fans.common.JsonData;
import com.fans.pojo.JobEntity;
import com.fans.service.interfaces.IDynamicJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @ClassName JobController
 * @Description: 调用中心控制层
 * @Author fan
 * @Date 2018-12-22 15:34
 * @Version 1.0
 **/
@RestController
@Slf4j
public class DynamicJobController {
    @Resource(type =SchedulerFactoryBean.class)
    private SchedulerFactoryBean factory;
    @Resource(name = "iDynamicJobService")
    private IDynamicJobService dynamicJobService;

    /**
     * @Description: 初始化所有Job
     * @Param: []
     * @return: void
     * @Author: fan
     * @Date: 2018/12/22 15:56
     **/
    @PostConstruct
    public void initialize() {
        try {
            restartAllJobs();
            log.info("quartz--> All Job Init Success");
        } catch (Exception e) {
            log.error("quartz--> All Job Init Error : {}", e.getMessage());
        }
    }

    @GetMapping(value = "/refresh/{id}")
    public JsonData<String> refresh(@PathVariable Integer id) {
        JobEntity entity = dynamicJobService.getJobEntityById(id);
        if (entity == null) {
            return JsonData.fail("调度任务不存在！！！");
        }
        TriggerKey triggerKey = new TriggerKey(entity.getName(), entity.getGroup());
        JobKey jobKey = dynamicJobService.getJobKey(entity);
        Scheduler scheduler = factory.getScheduler();
        try {
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            JobDataMap jobDataMap = dynamicJobService.getJobDataMap(entity);
            JobDetail jobDetail = dynamicJobService.getJobDetail(jobKey, entity.getDescription(), jobDataMap);
            if (StringUtils.equals(entity.getStatus(), "OPEN")) {
                scheduler.scheduleJob(jobDetail, dynamicJobService.getTrigger(entity));
                return JsonData.success("Refresh Job : " + entity.getName() + "\\t jarPath: " + entity.getJarPath() + " success !");
            } else {
                return JsonData.fail("Refresh Job : " + entity.getName() + "\\t jarPath: " + entity.getJarPath() + " failed !, Because the Job status is " + entity.getStatus() + "");
            }
        } catch (Exception e) {
            log.error("quartz--> Error while Refresh : {}", e.getMessage());
            return JsonData.fail("Error while Refresh " + e.getMessage() + "");
        }
    }

    @GetMapping(value = "/refresh/all")
    public JsonData<String> refreshAll() {
        try {
            restartAllJobs();
            return JsonData.success("Refresh All Job Success");
        } catch (Exception e) {
            log.error("quartz--> Error while Refresh : {}", e.getMessage());
            return JsonData.fail("Error while Refresh " + e.getMessage() + "");
        }
    }

    /**
     * @Description: 重启所有Job
     * @Param: []
     * @return: void
     * @Author: fan
     * @Date: 2018/12/22 16:15
     **/
    private void restartAllJobs() {
        Scheduler scheduler = factory.getScheduler();
        try {
            Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
            for (JobKey jobKey : jobKeySet) {
                scheduler.deleteJob(jobKey);
            }
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
