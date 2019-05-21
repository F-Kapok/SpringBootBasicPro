package com.fans.service.impl;

import com.fans.dao.UserMapper;
import com.fans.exception.ParamException;
import com.fans.pojo.User;
import com.fans.service.interfaces.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description:
 * @Author fan
 * @Date 2018-12-19 17:14
 * @Version 1.0
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Resource(type = com.fans.dao.UserMapper.class)
    private UserMapper userMapper;

    @Override
    public PageInfo<User> getList() {
        PageHelper.startPage(1, 5);
        return PageInfo.of(userMapper.selectAll());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUser(User user) {
        if (user == null) {
            user = User.builder()
                    .id(10L)
                    .username("jom")
                    .password("jom")
                    .desc("我是 jom skr")
                    .status(1)
                    .build();
        }
        return userMapper.insert(user);
    }


    @Override
    public int deleteUser(Long id) {
        id = 10L;
        userMapper.deleteByPrimaryKey(id);
        throw new ParamException(id + "错误");
    }

}
