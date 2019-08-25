package com.fans.config;

import com.fans.threadpool.basic.PoolRegister;
import com.fans.utils.JsonUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ThreadPoolConfiguration
 * @Description:
 * @Author k
 * @Date 2019-08-25 15:11
 * @Version 1.0
 **/
@Configuration
@Slf4j
public class ThreadPoolConfiguration {
    @Bean(name = "poolRegister")
    public PoolRegister poolRegister() {
        return new PoolRegister();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext applicationContext) {
        return args -> {
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            List<String> beanNameList = Lists.newArrayList(beanNames);
            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            File file = new File("pom.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            Model mavenModel = mavenXpp3Reader.read(fileInputStream);
            beanNameList = beanNameList.stream().filter(beanName -> applicationContext.getBean(beanName).getClass().getName().contains(mavenModel.getGroupId())).collect(Collectors.toList());
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            beanNameList.forEach(beanName -> {
                Object bean = applicationContext.getBean(beanName);
                builder.put(beanName, bean.getClass().getName());
            });
            ImmutableMap<String, String> map = builder.build();
            log.info("\r\n--> Let's inspect the beans provided by Spring Boot to project: \r\n{}", JsonUtils.obj2FormattingString(map));
        };
    }
}
