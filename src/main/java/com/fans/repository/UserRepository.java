package com.fans.repository;

import com.fans.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @InterfaceName UserRepository
 * @Description:
 * @Author fan
 * @Date 2019-03-29 14:37
 * @Version 1.0
 **/
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String userName, String passWord);

    /**
     * 批量更新
     * User 不是表名而是对象
     *
     * @param status
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "update User set status = ?1 where id in ?2")
    @Transactional(rollbackFor = Exception.class)
    int updateUserStatusByIds(Integer status, List<Long> ids);

    /**
     * 批量删除
     * nativeQuery = true 开启原生sql
     *
     * @param appIdList
     * @return
     */
    @Modifying
    @Query(value = "delete from user where id in ?1", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    int deleteUserByIds(List<Long> appIdList);
}
