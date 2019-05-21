package com.fans.service.impl;

import com.fans.service.interfaces.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Resource(name = "iUserService")
    private IUserService iUserService;

    @Test
    public void addUser() {
        iUserService.addUser(null);
    }
}