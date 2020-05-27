package com.fans.service.interfaces;

import com.fans.pojo.JobEntity;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.List;

/**
 * interfaceName: IDynamicJobService
 *
 * @author k
 * @version 1.0
 * @description 调度中心服务层
 * @date 2019-03-29 14:37
 **/
public interface IDynamicJobService {

    /**
     * description: 通过Id获取Job
     *
     * @param id 任务Id
     * @return com.fans.pojo.JobEntity
     * @author k
     * @date 2018-12-20 14:14
     **/
    JobEntity getJobEntityById(Integer id);

    /**
     * description: 从数据库中加载获取到所有Job
     *
     * @return java.util.List<com.fans.pojo.JobEntity>
     * @author k
     * @date 2018-12-20 14:14
     **/
    List<JobEntity> loadJobs();

    /**
     * description: 获取JobDataMap.(Job参数对象)
     *
     * @param job 任务实体
     * @return org.quartz.JobDataMap
     * @author k
     * @date 2018-12-20 14:14
     **/
    JobDataMap getJobDataMap(JobEntity job);

    /**
     * description: 获取JobDetail,JobDetail是任务的定义
     * 而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
     *
     * @param jobKey      jobKey
     * @param description 描述
     * @param map         任务入参
     * @return org.quartz.JobDetail
     * @author k
     * @date 2018-12-20 14:14
     **/
    JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map);

    /**
     * description: 获取Trigger (Job的触发器,执行规则)
     *
     * @param job 任务体
     * @return org.quartz.Trigger
     * @author k
     * @date 2018-12-20 14:14
     **/
    Trigger getTrigger(JobEntity job);

    /**
     * description: 获取JobKey, 包含Name和Group
     *
     * @param job 任务实体
     * @return org.quartz.JobKey
     * @author k
     * @date 2018-12-20 14:14
     **/
    JobKey getJobKey(JobEntity job);
}
