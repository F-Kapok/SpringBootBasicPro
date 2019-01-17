package com.fans.service.interfaces;

import com.fans.pojo.JobEntity;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.List;

/**
 * @InterfaceName IDynamicJobService
 * @Description: 调度中心服务层
 * @Author fan
 * @Date 2018-12-22 14:59
 * @Version 1.0
 **/
public interface IDynamicJobService {
    /**
     * 通过Id获取Job
     *
     * @param id
     * @return
     */
    JobEntity getJobEntityById(Integer id);

    /**
     * 从数据库中加载获取到所有Job
     *
     * @return
     */
    List<JobEntity> loadJobs();

    /**
     * 获取JobDataMap.(Job参数对象)
     *
     * @param job
     * @return
     */
    JobDataMap getJobDataMap(JobEntity job);

    /**
     * 获取JobDetail,JobDetail是任务的定义
     * 而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
     *
     * @param jobKey
     * @param description
     * @param map
     * @return
     */
    JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map);

    /**
     * 获取Trigger (Job的触发器,执行规则)
     *
     * @param job
     * @return
     */
    Trigger getTrigger(JobEntity job);

    /**
     * 获取JobKey, 包含Name和Group
     *
     * @param job
     * @return
     */
    JobKey getJobKey(JobEntity job);
}
