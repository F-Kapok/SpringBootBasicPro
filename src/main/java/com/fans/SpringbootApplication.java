package com.fans;

import com.fans.annotation.EnableHttpClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * className: SpringbootApplication
 *
 * @author k
 * @version 1.0
 * @description 入口
 * @date 2018-12-20 14:14
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.fans.dao")
@EnableHttpClient
@ServletComponentScan
@EnableAsync
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}

