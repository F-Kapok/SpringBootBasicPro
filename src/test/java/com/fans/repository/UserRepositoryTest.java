package com.fans.repository;

import com.fans.pojo.User;
import com.fans.utils.JsonUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserRepositoryTest {
    @Resource(type = UserRepository.class)
    private UserRepository userRepository;

    @Test
    public void findByUsernameAndPassword() {
        String username = "admin";
        String password = "admin";
        User user = userRepository.findByUsernameAndPassword(username, password);
        assertNotNull(user);
    }

    @Test
    public void updateUserStatusByIds() {
        Integer status = 0;
        List<Long> idList = Lists.newArrayList();
        idList.add(1L);
        idList.add(2L);
        idList.add(3L);
        idList.add(10L);
        int row = userRepository.updateUserStatusByIds(status, idList);
        assertNotEquals(row, 0);
    }

    @Test
    public void saveUser() {
        User user = User.builder()
                .id(4L)
                .username("kapok")
                .password("kapok")
                .desc("你好 kapok")
                .status(0)
                .build();
        User save = userRepository.save(user);
        assertNotNull(save);
    }

    @Test
    public void deleteUserByIds() {
        List<Long> ids = Lists.newArrayList();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        ids.add(4L);
        ids.add(10L);
        int row = userRepository.deleteUserByIds(ids);
        assertNotEquals(row, 0);
    }

    @Test
    public void findAllAndPage() {
        //jpa分页的第一页为0  前端一般传参为1
        int page = 1;
        int size = 2;
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<User> userInfoPage = userRepository.findAll(pageRequest);
        log.info("--> 转换后的字符串：{}", JsonUtils.obj2String(userInfoPage));
        assertNotNull(userInfoPage);
    }
}