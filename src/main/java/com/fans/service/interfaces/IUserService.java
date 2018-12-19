package com.fans.service.interfaces;

import com.fans.pojo.User;

import java.util.List;

/**
 * @InterfaceName IUserService
 * @Description: TODO
 * @Author fan
 * @Date 2018-12-19 17:13
 * @Version 1.0
 **/
public interface IUserService {
    List<User> getList();
}
