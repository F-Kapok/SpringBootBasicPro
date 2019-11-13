package com.fans.utils.xml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ReadXml
 * @Description: 读取xml到list集合
 * @Author k
 * @Date 2018-11-13 19:47
 * @Version 1.0
 **/
@Slf4j
public class ReadXml<T> {

    private String filePath;

    public ReadXml(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 读取xml中的数据放入集合
     *
     * @param fileName 文件名
     * @return 数据集合
     */
    public List<T> readXmlToList(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return Lists.newArrayList();
        }
        List<T> list = Lists.newArrayList();
        String suffix = ".xml";
        if (!fileName.endsWith(suffix)) {
            fileName = fileName + suffix;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        SAXReader reader = new SAXReader();
        reader.setEncoding(StandardCharsets.UTF_8.name());
        try {
            Document doc = reader.read(filePath + fileName);
            Element element = doc.getRootElement();
            Iterator iterator = element.elementIterator();
            while (iterator.hasNext()) {
                Map<String, String> map = Maps.newHashMap();
                Element father = (Element) iterator.next();
                Iterator son = father.elementIterator();
                if (!son.hasNext()) {
                    return Lists.newArrayList();
                }
                while (son.hasNext()) {
                    Element property = (Element) son.next();
                    map.put(property.getName(), property.getText());
                }
                String json = objectMapper.writeValueAsString(map);
                list.add(objectMapper.readValue(json, new TypeReference<T>() {
                    @Override
                    public Type getType() {
                        return super.getType();
                    }
                }));
            }
        } catch (Exception e) {
            log.error("--> 读取xml到list集合出错：{}", e.getMessage(), e);
            return Lists.newArrayList();
        }
        return list;
    }

    /**
     * 读取xml中的数据放入model
     *
     * @param fileName 文件名
     * @return 对象
     */
    public T readXmlToModel(String fileName, Class<T> tClass) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        String suffix = ".xml";
        if (!fileName.endsWith(suffix)) {
            fileName = fileName + suffix;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        SAXReader reader = new SAXReader();
        reader.setEncoding(StandardCharsets.UTF_8.name());
        try {
            Document doc = reader.read(filePath + fileName);
            Element element = doc.getRootElement();
            Iterator<Element> iterator = element.elementIterator();
            Map<String, String> map = Maps.newHashMap();
            while (iterator.hasNext()) {
                Element father = iterator.next();
                map.put(father.getName(), father.getText());
            }
            String json = objectMapper.writeValueAsString(map);
            return objectMapper.readValue(json, tClass);
        } catch (Exception e) {
            return null;
        }
    }
}
