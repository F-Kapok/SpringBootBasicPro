package com.fans.dao;

import com.fans.pojo.JobEntity;

import java.util.List;

public interface JobEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobEntity record);

    int insertSelective(JobEntity record);

    JobEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobEntity record);

    int updateByPrimaryKey(JobEntity record);

    List<JobEntity> selectAll();
}