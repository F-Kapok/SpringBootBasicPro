package com.fans.service.interfaces;

import com.fans.pojo.User;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * interfaceName: IUserService
 *
 * @author k
 * @version 1.0
 * @description 调度中心服务层
 * @date 2019-03-29 14:37
 **/
public interface IUserService {
    /**
     * description: fetch data by rule id
     *
     * @return com.github.pagehelper.PageInfo<com.fans.pojo.User>
     * @author k
     * @date 2018/12/20 10:13
     **/
    PageInfo<User> getList();

    /**
     * description: 增加人员
     *
     * @param user 用户实体
     * @return int
     * @author k
     * @date 2018/12/20 10:13
     **/
    int addUser(User user);

    /**
     * description: 删除人员
     *
     * @param id 用户Id
     * @return int
     * @author k
     * @date 2018/12/20 10:13
     **/
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    int deleteUser(Long id);
}
