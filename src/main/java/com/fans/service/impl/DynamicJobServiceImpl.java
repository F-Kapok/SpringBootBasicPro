package com.fans.service.impl;

import com.fans.common.CommonConstants;
import com.fans.dao.JobEntityMapper;
import com.fans.pojo.JobEntity;
import com.fans.quartz.DynamicJob;
import com.fans.service.interfaces.IDynamicJobService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName DynamicJobServiceImpl
 * @Description: TODO 调度中心实现层
 * @Author fan
 * @Date 2018-12-22 15:04
 * @Version 1.0
 **/
@Service("iDynamicJobService")
public class DynamicJobServiceImpl implements IDynamicJobService {
    @Resource(type = com.fans.dao.JobEntityMapper.class)
    private JobEntityMapper jobEntityMapper;

    @Override
    public JobEntity getJobEntityById(Integer id) {
        return jobEntityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<JobEntity> loadJobs() {
        return jobEntityMapper.selectAll();
    }

    @Override
    public JobDataMap getJobDataMap(JobEntity job) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("name", job.getName());
        jobDataMap.put("group", job.getGroup());
        jobDataMap.put("cronExpression", job.getCron());
        jobDataMap.put("parameter", job.getParameter());
        jobDataMap.put("JobDescription", job.getDescription());
        jobDataMap.put("vmParam", job.getVmParam());
        jobDataMap.put("jarPath", job.getJarPath());
        jobDataMap.put("status", job.getStatus());
        jobDataMap.put("jobType", job.getJobType());
        jobDataMap.put("className", job.getClassName());
        jobDataMap.put("classMethod", job.getClassMethod());
        return jobDataMap;
    }

    @Override
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    @Override
    public Trigger getTrigger(JobEntity job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getName(), job.getGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .build();
    }

    @Override
    public JobKey getJobKey(JobEntity job) {
        return JobKey.jobKey(job.getName(), job.getGroup());
    }
}
