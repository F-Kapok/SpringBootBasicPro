package com.fans.dao;

import com.fans.pojo.User;

import java.util.List;

public interface UserMapper {

    List<User> selectAll();

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}