package com.fans.utils;

import com.fans.common.JsonData;
import com.fans.pojo.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class JsonMapperTest {

    @Test
    public void obj2String() {
        List<User> users = Lists.newArrayList();
        User user = User.builder()
                .id(1L)
                .status(1)
                .descn("你好我是1")
                .username("kapok")
                .password("123123")
                .build();
        User user2 = User.builder()
                .id(2L)
                .status(2)
                .descn("你好我是2")
                .username("admin")
                .password("admin")
                .build();
        users.add(user);
        users.add(user2);
        JsonData jsonData = JsonData.success("祭司塞瑟斯", users);
        log.info(JsonMapper.obj2String(jsonData));
    }

    @Test
    public void string2Obj() {
        List<User> users = Lists.newArrayList();
        User user = User.builder()
                .id(1L)
                .status(1)
                .descn("你好我是1")
                .username("kapok")
                .password("123123")
                .build();
        User user2 = User.builder()
                .id(2L)
                .status(2)
                .descn("你好我是2")
                .username("admin")
                .password("admin")
                .build();
        users.add(user);
        users.add(user2);
        JsonData jsonData = JsonData.success("祭司塞瑟斯", users);
        String s = JsonMapper.obj2String(jsonData);
        String s2=JsonMapper.obj2String(users);
        JsonData j = JsonMapper.string2Obj(s, JsonData.class);
        List<User> users1 = JsonMapper.string2Obj(s2, new TypeReference<List<User>>() {
        });
        log.info(String.valueOf(j.isSuccess()));
        log.info(String.valueOf(users1.size()));
    }

    @Test
    public void string2Obj1() {
    }
}