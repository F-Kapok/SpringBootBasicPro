package com.fans;

import com.fans.annotation.EnableHttpClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 入口
 * @Param:
 * @return:
 * @Author: fan
 * @Date: 2018/12/19 17:19
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.fans.dao")
@EnableHttpClient
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}

