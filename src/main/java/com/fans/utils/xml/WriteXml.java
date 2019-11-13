package com.fans.utils.xml;

import com.fans.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName WriteXml
 * @Description: 生成xml
 * @Author k
 * @Date 2018-11-13 19:47
 * @Version 1.0
 **/
@Slf4j
public class WriteXml<T> {

    private String filePath;

    public WriteXml(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 将对象信息写入xml
     *
     * @param fileName   文件名
     * @param parentNode 父节点
     * @param model      对象信息
     */
    public void writeModelToXml(String fileName, String parentNode, T model) {
        long start = System.currentTimeMillis();
        if (StringUtils.isBlank(parentNode)) {
            parentNode = "data";
        }
        //新建空白xml并增加父标签
        fileName = createXmlInit(fileName, parentNode);
        SAXReader reader = new SAXReader();
        String file = filePath + fileName;
        try {
            //新建xml文件
            Document doc = reader.read(file);
            Element root = doc.getRootElement();
            generateNode(model, "", root);
            writeFile(file, doc);
        } catch (Exception e) {
            log.error("--> 写入数据到xml报错：{}", e.getMessage(), e);
        }
        log.info("--> 生成[{}]文件成功，耗时：{}ms，全路径：{}", fileName, System.currentTimeMillis() - start, file);
    }

    /**
     * 将list写入xml
     *
     * @param fileName   文件名
     * @param parentNode 父节点
     * @param sonNode    子节点
     * @param list       数据源
     */
    public void writeListToXml(String fileName, String parentNode, String sonNode, List<T> list) {
        long start = System.currentTimeMillis();
        if (StringUtils.isBlank(parentNode)) {
            parentNode = "root";
        }
        if (StringUtils.isBlank(sonNode)) {
            sonNode = "data";
        }
        final String node = sonNode;
        //新建空白xml并增加父标签
        fileName = createXmlInit(fileName, parentNode);
        SAXReader reader = new SAXReader();
        String file = filePath + fileName;
        try {
            //新建xml文件
            Document doc = reader.read(file);
            Element root = doc.getRootElement();
            list.forEach(t -> generateNode(t, node, root));
            writeFile(file, doc);
        } catch (Exception e) {
            log.error("--> 写入数据到xml报错：{}", e.getMessage(), e);
        }
        log.info("--> 生成[{}]文件成功，耗时：{}ms，全路径：{}", fileName, System.currentTimeMillis() - start, file);
    }

    /**
     * 新建初始xml文件 并返回文件名
     *
     * @param fileName   文件名
     * @param parentNode 父节点
     * @return 文件名
     */
    private String createXmlInit(String fileName, String parentNode) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd(HH-mm-ss)");
        String suffix = ".xml";
        //默认文件名
        if (StringUtils.isBlank(fileName)) {
            fileName = format.format(date);
        }
        if (!fileName.endsWith(suffix)) {
            fileName = fileName + suffix;
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath + fileName), StandardCharsets.UTF_8)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.write("\n");
            writer.write("<" + parentNode + ">\n");
            writer.write("</" + parentNode + ">\n");
            writer.flush();
        } catch (Exception e) {
            log.error("--> 初始化xml文件出错：{}", e.getMessage(), e);
        }
        return fileName;
    }

    /**
     * 返回数据中的key列表
     *
     * @param map 数据源
     * @return List<Key>
     */
    private List<String> getMapKeyToList(Map<String, String> map) {
        List<String> result = Lists.newArrayList();
        Set<String> set = map.keySet();
        result.addAll(set);
        return result;
    }

    /**
     * 生成标签
     *
     * @param model 数据模板
     * @param node  标签节点名
     * @param root  父标签
     */
    private void generateNode(T model, String node, Element root) {
        //转化为Map<String,String>形式
        String jsonStr = JsonUtils.obj2String(model);
        Map<String, String> map = JsonUtils.string2Obj(jsonStr, new TypeReference<Map<String, String>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
        // 添加子标签  node为空则默认加到父标签里
        if (StringUtils.isNotBlank(node)) {
            Element label = root.addElement(node);
            getMapKeyToList(map).forEach(key -> {
                // 添加属性标签
                Element property = label.addElement(key);
                // 添加文本; 设置文本属性 : property.addAttribute("test", "test1")
                property.addText(map.get(key));
            });
        } else {
            getMapKeyToList(map).forEach(key -> {
                // 添加属性标签
                Element property = root.addElement(key);
                // 添加文本; 设置文本属性 : property.addAttribute("test", "test1")
                property.addText(map.get(key));
            });
        }
    }

    /**
     * 将内容写入指定xml文件
     *
     * @param file 文件路径+文件名
     * @param doc  要生成的数据
     */
    private void writeFile(String file, Document doc) {
        try (PrintWriter writer = new PrintWriter(file)) {
            doc.write(writer);
            writer.flush();
        } catch (IOException e) {
            log.error("--> 写入数据到文件报错：{}", e.getMessage(), e);
        }
    }
}
