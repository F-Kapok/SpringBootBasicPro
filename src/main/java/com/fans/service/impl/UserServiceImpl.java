package com.fans.service.impl;

import com.fans.dao.UserMapper;
import com.fans.pojo.User;
import com.fans.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2018-12-19 17:14
 * @Version 1.0
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getList() {
        List<User> users = userMapper.selectAll();
        return users;
    }
}
