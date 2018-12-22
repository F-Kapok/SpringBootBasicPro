package com.fans.service.interfaces;

import com.fans.pojo.JobEntity;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.List;

/**
 * @InterfaceName IDynamicJobService
 * @Description: TODO 调度中心服务层
 * @Author fan
 * @Date 2018-12-22 14:59
 * @Version 1.0
 **/
public interface IDynamicJobService {
    /**
     * @Description: TODO 通过Id获取Job
     * @Param: [id]
     * @return: com.fans.pojo.JobEntity
     * @Author: fan
     * @Date: 2018/12/22 15:01
     **/
    JobEntity getJobEntityById(Integer id);

    /**
     * @Description: TODO 从数据库中加载获取到所有Job
     * @Param: []
     * @return: java.util.List<com.fans.pojo.JobEntity>
     * @Author: fan
     * @Date: 2018/12/22 15:02
     **/
    List<JobEntity> loadJobs();

    /**
     * @Description: TODO 获取JobDataMap.(Job参数对象)
     * @Param: [job]
     * @return: org.quartz.JobDataMap
     * @Author: fan
     * @Date: 2018/12/22 15:02
     **/
    JobDataMap getJobDataMap(JobEntity job);

    /**
     * @Description: TODO 获取JobDetail,JobDetail是任务的定义
     * @Description: TODO 而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
     * @Param: [jobKey, description, map]
     * @return: org.quartz.JobDetail
     * @Author: fan
     * @Date: 2018/12/22 15:02
     **/
    JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map);

    /**
     * @Description: TODO 获取Trigger (Job的触发器,执行规则)
     * @Param: [job]
     * @return: org.quartz.Trigger
     * @Author: fan
     * @Date: 2018/12/22 15:03
     **/
    Trigger getTrigger(JobEntity job);

    /**
     * @Description: TODO 获取JobKey,包含Name和Group
     * @Param: [job]
     * @return: org.quartz.JobKey
     * @Author: fan
     * @Date: 2018/12/22 15:03
     **/
    JobKey getJobKey(JobEntity job);
}
