package com.fans.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName ConfigProperties
 * @Description: config properties数据读取
 * @Author fan
 * @Date 2018-12-20 12:40
 * @Version 1.0
 **/
@Component(value = "configProperties")
@PropertySource(value = "classpath:/properties/config.properties")
@ConfigurationProperties(prefix = "com.kapok")
@Setter
@Getter
public class ConfigProperties {
    private String host;
}
