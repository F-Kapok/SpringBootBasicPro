package com.fans.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;


/**
 * className: PropertiesUtils
 *
 * @author k
 * @version 1.0
 * @description 读取properties配置文件工具
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class PropertiesUtils {

    private static Properties properties;

    private static final String PROPERTIES_SUFFIX = ".properties";

    private static final String PROPERTIES_PATH = "properties/";

    public static String loadProperties(String fileName, String key) {
        initializeProperties(fileName);
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    public static String loadProperties(String fileName, String key, String defaultValue) {
        initializeProperties(fileName);
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return defaultValue.trim();
        }
        return value.trim();
    }

    public static Map<String, String> loadProperties(String fileName) {
        Map<String, String> map = new LinkedHashMap<>();
        Reader reader = initializeReader(fileName);
        try {
            assert reader != null;
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.startsWith("#")) {
                    String name = line.substring(0, line.indexOf("="));
                    String value = line.substring(line.indexOf("=") + 1);
                    if ((StringUtils.isNotBlank(name)) && (StringUtils.isNotBlank(value))) {
                        map.put(name.trim(), value.trim());
                    }
                }
                line = br.readLine();
            }
            return map;
        } catch (Exception e) {
            log.error("配置文件内容映射Map异常", e);
            return null;
        }
    }

    private static InputStreamReader initializeReader(String fileName) {
        try {
            if (!fileName.endsWith(PROPERTIES_SUFFIX)) {
                fileName = autoJoint(fileName);
            }
            Resource resource = new ClassPathResource(PROPERTIES_PATH + fileName);
            InputStream in = resource.getInputStream();
            return new InputStreamReader(in, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("配置文件读取异常", e);
            return null;
        }
    }

    private static void initializeProperties(String fileName) {
        properties = new Properties();
        InputStreamReader reader = initializeReader(fileName);
        try {
            properties.load(reader);
        } catch (IOException e) {
            log.error("配置文件读取异常", e);
        }
    }

    private static String autoJoint(String fileName) {
        return fileName + PROPERTIES_SUFFIX;
    }
}
