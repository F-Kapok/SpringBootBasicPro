package com.fans.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * className: ConfigProperties
 *
 * @author k
 * @version 1.0
 * @description config properties数据读取
 * @date 2018-12-20 14:14
 **/
@Component(value = "configProperties")
@PropertySource(value = "classpath:/properties/config.properties")
@ConfigurationProperties(prefix = "com.kapok")
@Setter
@Getter
public class ConfigProperties {
    private String host;
}
