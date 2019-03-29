package com.fans.service.interfaces;

import com.fans.pojo.User;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @InterfaceName IUserService
 * @Description:
 * @Author fan
 * @Date 2018-12-19 17:13
 * @Version 1.0
 **/
public interface IUserService {
    /**
     * fetch data by rule id
     *
     * @Description:
     * @Param: []
     * @return: / java.util.List<com.fans.pojo.User>
     * @Author: fan
     * @Date: 2018/12/20 10:13
     **/
    PageInfo<User> getList();

    /**
     * 增加人员
     *
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 删除人员
     *
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    int deleteUser(Long id);
}
