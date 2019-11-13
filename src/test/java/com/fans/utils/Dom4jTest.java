package com.fans.utils;

import com.fans.utils.xml.ReadXml;
import com.fans.utils.xml.WriteXml;
import com.fans.utils.xml.model.User;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @ClassName Dom4jTest
 * @Description:
 * @Author k
 * @Date 2018-11-13 15:17
 * @Version 1.0
 **/
public class Dom4jTest {
    @Test
    public void writeXml() {
        WriteXml<Map<String, String>> writeXml1 = new WriteXml<>("E:\\springboot\\src\\test\\java\\com\\fans\\utils\\");
        //List<Map>类型
        List<Map<String, String>> listMap = Lists.newArrayList(
                ImmutableMap.of("name", "张三", "age", "18"),
                ImmutableMap.of("name", "李四", "age", "18"),
                ImmutableMap.of("name", "王五", "age", "18")
        );
        writeXml1.writeListToXml("List_Map_Test", "", "", listMap);
        //List<Model>类型
        WriteXml<User> writeXml2 = new WriteXml<>("E:\\springboot\\src\\test\\java\\com\\fans\\utils\\");
        List<User> listModel = Lists.newArrayList(
                User.builder().name("张三").age(18).build(),
                User.builder().name("李四").age(17).build(),
                User.builder().name("王五").age(16).build()
        );
        writeXml2.writeListToXml("List_Model_Test", "", "", listModel);
        //Model类型
        User jack = User.builder().age(13).name("Jack").build();
        writeXml2.writeModelToXml("Model_Test", "", jack);
    }

    @Test
    public void readXml() {
        //List<Map>类型
        ReadXml<Map<String, String>> readXml1 = new ReadXml<>("E:\\springboot\\src\\test\\java\\com\\fans\\utils\\");
        List<Map<String, String>> test1 = readXml1.readXmlToList("List_Map_Test");
        System.out.println(test1.toString());
        //List<Model>类型
        ReadXml<User> readXml2 = new ReadXml<>("E:\\springboot\\src\\test\\java\\com\\fans\\utils\\");
        List<User> test2 = readXml2.readXmlToList("List_Model_Test");
        System.out.println(test2.toString());
        //Model类型
        ReadXml<User> readXml3 = new ReadXml<>("E:\\springboot\\src\\test\\java\\com\\fans\\utils\\");
        User test3 = readXml3.readXmlToModel("Model_Test", User.class);
        System.out.println(test3.toString());
    }
}
