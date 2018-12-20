package com.fans.service.interfaces;

import com.fans.pojo.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @InterfaceName IUserService
 * @Description: TODO
 * @Author fan
 * @Date 2018-12-19 17:13
 * @Version 1.0
 **/
public interface IUserService {
    /**
     * fetch data by rule id
     *
     * @Description: TODO
     * @Param: []
     * @return: / java.util.List<com.fans.pojo.User>
     * @Author: fan
     * @Date: 2018/12/20 10:13
     **/
    List<User> getList();

    /**
     * @return int
     * @Description TODO
     * @Param [user]
     * @Author fan
     * @Date 2018/12/20 10:24
     **/
    int addUser(User user);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    int deleteUser(Long id);
}
