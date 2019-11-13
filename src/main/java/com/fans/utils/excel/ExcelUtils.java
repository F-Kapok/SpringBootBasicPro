package com.fans.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fans.utils.excel.listener.ExcelListener;
import com.fans.utils.excel.param.MultipleSheetProperty;
import com.fans.utils.excel.param.ReadExcelParam;
import com.fans.utils.excel.param.WriteExcelParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @ClassName ExcelUtils
 * @Description: 阿里excel工具封装
 * @Author k
 * @Date 2019-11-06 09:55
 * @Version 1.0
 **/
@Slf4j
public class ExcelUtils {
    /**
     * 有模板,读小于1000行数据, 带样式
     * <p>
     * sheetNo       sheet页码，默认为 0
     * headRowNumber 第几行开始读取数据，默认为0, 表示从第一行开始读取
     * filePath      文件绝对路径
     * model         模板
     *
     * @return excel表中数据集合
     */
    public static <T> List<T> readLessThan1000RowByModel(ReadExcelParam<T> readExcelParam) {
        int sheetNo = readExcelParam.getSheetNo();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        Class<T> model = readExcelParam.getModel();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (model == null) {
            throw new RuntimeException("excel模板不能为null");
        }
        try (InputStream fileStream = new FileInputStream(filePath)) {
            return EasyExcel.read(fileStream, model, null).sheet(sheetNo).headRowNumber(headRowNumber).autoTrim(true).doReadSync();
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 无模板,读小于1000行数据, 带样式
     * <p>
     * sheetNo       sheet页码，默认为 0
     * headRowNumber 第几行开始读取数据，默认为0, 表示从第一行开始读取
     * filePath      文件绝对路径
     * excel表中数据集合
     */
    public static List<JSONObject> readLessThan1000Row(ReadExcelParam readExcelParam) {
        int sheetNo = readExcelParam.getSheetNo();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        try (InputStream fileStream = new FileInputStream(filePath)) {
            List<Object> result = EasyExcel.read(fileStream).sheet(sheetNo).headRowNumber(headRowNumber).autoTrim(true).doReadSync();
            return result.stream().map(o -> JSON.parseObject(JSON.toJSONString(o))).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 有模板,读大于1000行数据, 带样式
     * <p>
     * sheetNo       sheet页码，默认为 0
     * headRowNumber 第几行开始读取数据，默认为0, 表示从第一行开始读取
     * filePath      文件绝对路径
     * listener      解析器
     * model         模板
     *
     * @return excel表中数据集合
     */
    public static <T> List<T> readMoreThan1000RowByModel(ReadExcelParam<T> readExcelParam) {
        int sheetNo = readExcelParam.getSheetNo();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        Class<? extends AnalysisEventListener> listener = readExcelParam.getListener();
        Class<T> model = readExcelParam.getModel();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (listener == null) {
            throw new RuntimeException("excel解析器不能为null");
        }
        if (model == null) {
            throw new RuntimeException("excel模板不能为null");
        }
        try (InputStream fileStream = new FileInputStream(filePath)) {
            AnalysisEventListener analysisEventListener = listener.newInstance();
            Method method = analysisEventListener.getClass().getMethod("getDataList");
            EasyExcel.read(fileStream, model, analysisEventListener).sheet(sheetNo).headRowNumber(headRowNumber).autoTrim(true).doRead();
            return (List<T>) method.invoke(analysisEventListener);
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error("--> 反射解析监听器出错：{}", e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            log.error("--> 反射解析监获取数据方法出错：{}", e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error("--> 反射解析监执行数据方法出错：{}", e.getMessage(), e);
        } catch (InstantiationException e) {
            log.error("--> 反射解析监方法出错：{}", e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 无模板,读大于1000行数据, 带样式
     * <p>
     * sheetNo       sheet页码，默认为 0
     * headRowNumber 第几行开始读取数据，默认为0, 表示从第一行开始读取
     * filePath      文件绝对路径
     *
     * @return excel表中数据集合
     */
    public static List<JSONObject> readMoreThan1000Row(ReadExcelParam readExcelParam) {
        int sheetNo = readExcelParam.getSheetNo();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        Class listener = readExcelParam.getListener();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (listener == null) {
            throw new RuntimeException("excel解析器不能为null");
        }
        try (InputStream fileStream = new FileInputStream(filePath)) {
            AnalysisEventListener analysisEventListener = (AnalysisEventListener) listener.newInstance();
            Method method = analysisEventListener.getClass().getMethod("getDataList");
            EasyExcel.read(fileStream, analysisEventListener).sheet(sheetNo).headRowNumber(headRowNumber).autoTrim(true).doRead();
            return (List<JSONObject>) method.invoke(analysisEventListener);
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error("--> 反射解析监听器出错：{}", e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            log.error("--> 反射解析监获取数据方法出错：{}", e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error("--> 反射解析监执行数据方法出错：{}", e.getMessage(), e);
        } catch (InstantiationException e) {
            log.error("--> 反射解析监方法出错：{}", e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 有模板,读多个sheet的数据, 带样式
     * <p>
     * filePath      文件绝对路径
     * headRowNumber 第几行开始读取数据，默认为0, 表示从第一行开始读取
     * model         模板
     *
     * @return excel表中多个sheet的数据集合
     */
    public static <T> Map<Integer, List<T>> readMoreSheetByModel(ReadExcelParam<T> readExcelParam) {
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        Class<T> model = readExcelParam.getModel();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (model == null) {
            throw new RuntimeException("excel模板不能为null");
        }
        Map<Integer, List<T>> result = Maps.newHashMap();
        try (InputStream fileStream = new FileInputStream(filePath)) {
            ExcelListener<T> excelListener = new ExcelListener<>();
            ExcelReaderBuilder builder = EasyExcel.read(fileStream, model, excelListener);
            ExcelReader reader = builder.build();
            List<ReadSheet> readSheets = reader.excelExecutor().sheetList();
            readSheets.forEach(readSheet -> {
                readSheet.setHeadRowNumber(headRowNumber);
                readSheet.setAutoTrim(true);
                String sheetName = readSheet.getSheetName();
                Integer sheetNo = readSheet.getSheetNo();
                excelListener.getDataList().clear();
                log.info("--> sheetName : {}", sheetName);
                reader.read(readSheet);
                //读取每一个sheet的内容
                List<T> current = Lists.newArrayList();
                current.addAll(excelListener.getDataList());
                log.info("--> Put data:{}", JSONObject.toJSONString(current));
                //读取的数据放入map key为sheetNo
                result.put(sheetNo, current);
            });
            reader.finish();
            return result;
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Maps.newHashMap();
    }

    /**
     * 无模板,读多个sheet的数据, 带样式
     * <p>
     * filePath      文件绝对路径
     * headRowNumber 第几行开始读取数据，默认为0, 表示从第一行开始读取
     * excel表中多个sheet的数据集合
     */
    public static Map<Integer, List<JSONObject>> readMoreSheet(ReadExcelParam readExcelParam) {
        String filePath = readExcelParam.getFilePath();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        Map<Integer, List<JSONObject>> result = Maps.newHashMap();
        try (InputStream fileStream = new FileInputStream(filePath)) {
            ExcelListener<JSONObject> excelListener = new ExcelListener<>();
            ExcelReaderBuilder builder = EasyExcel.read(fileStream, excelListener);
            ExcelReader reader = builder.build();
            List<ReadSheet> readSheets = reader.excelExecutor().sheetList();
            readSheets.forEach(readSheet -> {
                readSheet.setHeadRowNumber(headRowNumber);
                readSheet.setAutoTrim(true);
                String sheetName = readSheet.getSheetName();
                Integer sheetNo = readSheet.getSheetNo();
                excelListener.getDataList().clear();
                log.info("--> sheetName : {}", sheetName);
                reader.read(readSheet);
                //读取每一个sheet的内容
                List<JSONObject> current = Lists.newArrayList();
                current.addAll(excelListener.getDataList());
                log.info("--> Put data:{}", JSONObject.toJSONString(current));
                //读取的数据放入map key为sheetNo
                result.put(sheetNo, current);
            });
            reader.finish();
            return result;
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Maps.newHashMap();
    }

    /**
     * 生成excel无指定模板-单个sheet
     * <p>
     * filePath  文件绝对路径
     * sheetName sheet名称
     * head      简单单行表头
     * data      数据源
     *
     * @return true：生成成功 false：生成失败
     */
    public static boolean writeSimpleExcel(WriteExcelParam<Object> writeExcelParam) {
        String filePath = writeExcelParam.getFilePath();
        String sheetName = writeExcelParam.getSheetName();
        List<String> simpleHead = writeExcelParam.getSimpleHead();
        List<List<String>> data = writeExcelParam.getData();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (StringUtils.isBlank(sheetName)) {
            throw new RuntimeException("sheetName名称不能为空");
        }
        if (simpleHead == null) {
            throw new RuntimeException("单行表头不能为null");
        }
        if (data == null) {
            throw new RuntimeException("数据源不能为null");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            List<List<String>> headList = Lists.newArrayList();
            simpleHead.forEach(h -> headList.add(Collections.singletonList(h)));
            EasyExcel.write(outputStream).sheet(sheetName).useDefaultStyle(true).needHead(true).head(headList).doWrite(data);
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
            return false;
        } catch (IOException e) {
            log.error("--。 excel文件导出失败, 失败原因：{}", e.getMessage(), e);
            return false;
        }
        log.info("--> 生成excel文件成功，文件地址：{}", filePath);
        return true;
    }

    /**
     * 生成excel无指定模板-单个sheet
     * <p>
     * filePath  文件绝对路径
     * sheetName sheet名称
     * head      复杂多行表头
     * data      数据源
     *
     * @return true：生成成功 false：生成失败
     */
    public static boolean writeHardExcel(WriteExcelParam<Object> writeExcelParam) {
        String filePath = writeExcelParam.getFilePath();
        String sheetName = writeExcelParam.getSheetName();
        List<List<String>> hardHead = writeExcelParam.getHardHead();
        List<List<String>> data = writeExcelParam.getData();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (StringUtils.isBlank(sheetName)) {
            throw new RuntimeException("sheetName名称不能为空");
        }
        if (hardHead == null) {
            throw new RuntimeException("多行表头不能为null");
        }
        if (data == null) {
            throw new RuntimeException("数据源不能为null");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            EasyExcel.write(outputStream).sheet(sheetName).useDefaultStyle(true).needHead(true).head(hardHead).doWrite(data);
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
            return false;
        } catch (IOException e) {
            log.error("--> excel文件导出失败, 失败原因：{}", e.getMessage(), e);
            return false;
        }
        log.info("--> 生成excel文件成功，文件地址：{}", filePath);
        return true;
    }

    /**
     * 生成excel有指定模板-单个sheet
     * <p>
     * filePath  文件绝对路径
     * sheetName sheet名称
     * data      数据源 以及 head
     * model     模板
     *
     * @return true：生成成功 false：生成失败
     */
    public static <T> boolean writeExcelByModel(WriteExcelParam<T> writeExcelParam) {
        String filePath = writeExcelParam.getFilePath();
        String sheetName = writeExcelParam.getSheetName();
        List<T> modelData = writeExcelParam.getModelData();
        Class<T> model = writeExcelParam.getModel();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (StringUtils.isBlank(sheetName)) {
            throw new RuntimeException("sheetName名称不能为空");
        }
        if (modelData == null) {
            throw new RuntimeException("数据源不能为null");
        }
        if (model == null) {
            throw new RuntimeException("模板不能为null");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            EasyExcel.write(outputStream, model).sheet(sheetName).doWrite(modelData);
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
            return false;
        } catch (IOException e) {
            log.error("--> excel文件导出失败, 失败原因：{}", e.getMessage(), e);
            return false;
        }
        log.info("--> 生成excel文件成功，文件地址：{}", filePath);
        return true;
    }

    /**
     * 生成多Sheet的excel-有模板
     * <p>
     * filePath                文件绝对路径
     * multipleSheetProperties 多sheet数据模板List
     * model                   模板
     *
     * @return true：生成成功 false：生成失败
     */
    public static <T> boolean writeWithMultipleSheet(WriteExcelParam<T> writeExcelParam) {
        String filePath = writeExcelParam.getFilePath();
        List<MultipleSheetProperty<T>> multipleSheetProperties = writeExcelParam.getMultipleSheetProperties();
        Class<T> model = writeExcelParam.getModel();
        Class<? extends CellWriteHandler> cellWriteHandler = writeExcelParam.getCellWriteHandler();
        Class<? extends SheetWriteHandler> sheetWriteHandler = writeExcelParam.getSheetWriteHandler();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (multipleSheetProperties == null) {
            throw new RuntimeException("多sheet的数据源不能为null");
        }
        if (model == null) {
            throw new RuntimeException("模板不能为null");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            AtomicInteger index = new AtomicInteger();
            ExcelWriterBuilder builder = EasyExcel.write(outputStream, model);
            if (cellWriteHandler != null) {
                //注册单元格拦截器
                builder.registerWriteHandler(cellWriteHandler.newInstance());
            }
            if (sheetWriteHandler != null) {
                //注册sheet拦截器
                builder.registerWriteHandler(sheetWriteHandler.newInstance());
            }
            ExcelWriter excelWriter = builder.build();
            multipleSheetProperties.forEach(multipleSheetProperty -> {
                WriteSheet writeSheet = EasyExcel.writerSheet(index.get(), multipleSheetProperty.getSheetName()).build();
                excelWriter.write(multipleSheetProperty.getData(), writeSheet);
                index.getAndIncrement();
            });
            excelWriter.finish();
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
            return false;
        } catch (IOException | InstantiationException e) {
            log.error("--> excel文件导出失败, 失败原因：{}", e.getMessage(), e);
            return false;
        } catch (IllegalAccessException e) {
            log.error("--> 反射创建拦截器失败, 失败原因：{}", e.getMessage(), e);
            return false;
        }
        log.info("--> 生成excel文件成功，文件地址：{}", filePath);
        return true;
    }

    /**
     * 生成多Sheet的excel-无模板
     * <p>
     * filePath                文件绝对路径
     * simpleHead              简单单行表头
     * multipleSheetProperties 多sheet数据模板List
     *
     * @return true：生成成功 false：生成失败
     */
    public static <T> boolean writeWithMultipleSheetNoModelSimple(WriteExcelParam<T> writeExcelParam) {
        String filePath = writeExcelParam.getFilePath();
        List<String> simpleHead = writeExcelParam.getSimpleHead();
        List<MultipleSheetProperty<T>> multipleSheetProperties = writeExcelParam.getMultipleSheetProperties();
        Class<? extends CellWriteHandler> cellWriteHandler = writeExcelParam.getCellWriteHandler();
        Class<? extends SheetWriteHandler> sheetWriteHandler = writeExcelParam.getSheetWriteHandler();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (multipleSheetProperties == null) {
            throw new RuntimeException("多sheet的数据源不能为null");
        }
        if (simpleHead == null) {
            throw new RuntimeException("单行表头不能为null");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            List<List<String>> headList = Lists.newArrayList();
            simpleHead.forEach(h -> headList.add(Collections.singletonList(h)));
            AtomicInteger index = new AtomicInteger();
            ExcelWriterBuilder builder = EasyExcel.write(outputStream);
            if (cellWriteHandler != null) {
                //注册单元格拦截器
                builder.registerWriteHandler(cellWriteHandler.newInstance());
            }
            if (sheetWriteHandler != null) {
                //注册sheet拦截器
                builder.registerWriteHandler(sheetWriteHandler.newInstance());
            }
            ExcelWriter excelWriter = builder.build();
            multipleSheetProperties.forEach(multipleSheetProperty -> {
                WriteSheet writeSheet = EasyExcel.writerSheet(index.get(), multipleSheetProperty.getSheetName())
                        .useDefaultStyle(true)
                        .needHead(true)
                        .head(headList)
                        .build();
                excelWriter.write(multipleSheetProperty.getData(), writeSheet);
                index.getAndIncrement();
            });
            excelWriter.finish();
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
            return false;
        } catch (IOException e) {
            log.error("--> excel文件导出失败, 失败原因：{}", e.getMessage(), e);
            return false;
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("--> 反射创建拦截器失败, 失败原因：{}", e.getMessage(), e);
            return false;
        }
        log.info("--> 生成excel文件成功，文件地址：{}", filePath);
        return true;
    }


    /**
     * 生成多Sheet的excel-无模板
     * <p>
     * filePath                文件绝对路径
     * hardHead                复杂多行表头
     * multipleSheetProperties 多sheet数据模板List
     *
     * @return true：生成成功 false：生成失败
     */
    public static <T> boolean writeWithMultipleSheetNoModelHard(WriteExcelParam<T> writeExcelParam) {
        String filePath = writeExcelParam.getFilePath();
        List<List<String>> hardHead = writeExcelParam.getHardHead();
        List<MultipleSheetProperty<T>> multipleSheetProperties = writeExcelParam.getMultipleSheetProperties();
        Class<? extends CellWriteHandler> cellWriteHandler = writeExcelParam.getCellWriteHandler();
        Class<? extends SheetWriteHandler> sheetWriteHandler = writeExcelParam.getSheetWriteHandler();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (multipleSheetProperties == null) {
            throw new RuntimeException("多sheet的数据源不能为null");
        }
        if (hardHead == null) {
            throw new RuntimeException("多行表头不能为null");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            AtomicInteger index = new AtomicInteger();
            ExcelWriterBuilder builder = EasyExcel.write(outputStream);
            if (cellWriteHandler != null) {
                //注册单元格拦截器
                builder.registerWriteHandler(cellWriteHandler.newInstance());
            }
            if (sheetWriteHandler != null) {
                //注册sheet拦截器
                builder.registerWriteHandler(sheetWriteHandler.newInstance());
            }
            ExcelWriter excelWriter = builder.build();
            multipleSheetProperties.forEach(multipleSheetProperty -> {
                WriteSheet writeSheet = EasyExcel.writerSheet(index.get(), multipleSheetProperty.getSheetName())
                        .useDefaultStyle(true)
                        .needHead(true)
                        .head(hardHead)
                        .build();
                excelWriter.write(multipleSheetProperty.getData(), writeSheet);
                index.getAndIncrement();
            });
            excelWriter.finish();
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
            return false;
        } catch (IOException e) {
            log.error("--> excel文件导出失败, 失败原因：{}", e.getMessage(), e);
            return false;
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("--> 反射创建拦截器失败, 失败原因：{}", e.getMessage(), e);
            return false;
        }
        log.info("--> 生成excel文件成功，文件地址：{}", filePath);
        return true;
    }
}
