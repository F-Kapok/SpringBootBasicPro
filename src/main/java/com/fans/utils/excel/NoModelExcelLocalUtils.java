package com.fans.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fans.utils.excel.base.AbstractExcelBaseListener;
import com.fans.utils.excel.param.read.ReadExcelParam;
import com.fans.utils.excel.param.read.ReadMultipleSheetParam;
import com.fans.utils.excel.param.write.MultipleFreedomSheetProperty;
import com.fans.utils.excel.param.write.WriteFreedomMultipleParam;
import com.fans.utils.excel.param.write.WriteFreedomParam;
import com.fans.utils.excel.strategy.ColumnWidthStyleStrategy;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * className: NewExcelUtils
 *
 * @author k
 * @version 1.0
 * @description 新版excel渲染
 * @date 2020-05-18 23:02
 **/
@Slf4j
public class NoModelExcelLocalUtils {

    /**
     * description:  无模板,读小于1000行数据, 带样式
     *
     * @param readExcelParam 单模板单sheet读取通用入参
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     * @author k
     * @date 2020/05/19 0:21
     **/
    public static List<JSONObject> readLessThan1000Row(ReadExcelParam<JSONObject> readExcelParam) {
        int sheetNo = readExcelParam.getSheetNo();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空!!!");
        }
        try (InputStream inputStream = new FileInputStream(filePath)) {
            List<Object> result = EasyExcel.read(inputStream)
                    .sheet(sheetNo)
                    .headRowNumber(headRowNumber)
                    .autoTrim(true)
                    .doReadSync();
            return result.stream().map(o -> JSON.parseObject(JSON.toJSONString(o))).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * description:  无模板,读大于1000行数据, 带样式
     *
     * @param readExcelParam 单模板单sheet读取通用入参
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     * @author k
     * @date 2020/05/19 0:43
     **/
    public static List<JSONObject> readMoreThan1000Row(ReadExcelParam<JSONObject> readExcelParam) {
        int sheetNo = readExcelParam.getSheetNo();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        AbstractExcelBaseListener<JSONObject> listener = readExcelParam.getListener();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        if (listener == null) {
            throw new RuntimeException("excel解析器不能为null");
        }
        try (InputStream fileStream = new FileInputStream(filePath)) {
            EasyExcel.read(fileStream, listener)
                    .sheet(sheetNo)
                    .headRowNumber(headRowNumber)
                    .autoTrim(true)
                    .doRead();
            return listener.getDataList();
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * description:  无模板,读多个sheet的数据, 带样式
     *
     * @param readMultipleSheetParam 多模板多sheet读取通用入参
     * @return java.util.Map<java.lang.Integer, java.util.List < com.alibaba.fastjson.JSONObject>>
     * @author k
     * @date 2020/05/19 9:10
     **/
    public static Map<Integer, List<JSONObject>> readMoreSheet(ReadMultipleSheetParam readMultipleSheetParam) {
        String filePath = readMultipleSheetParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        Map<Integer, List<JSONObject>> result = Maps.newHashMap();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            ExcelReader excelReader = EasyExcel.read(inputStream).build();
            List<ReadSheet> sheetList = excelReader.excelExecutor().sheetList();
            sheetList.forEach(readSheet -> {
                Integer sheetNo = readSheet.getSheetNo();
                String sheetName = readSheet.getSheetName();
                ImmutableMap<Integer, AbstractExcelBaseListener<?>> sheetNoAndListener = readMultipleSheetParam.getSheetNoAndListener();
                sheetNoAndListener = sheetNoAndListener == null ? ImmutableMap.of() : sheetNoAndListener;
                ImmutableMap<Integer, Integer> sheetNoAndHeadRowNumber = readMultipleSheetParam.getSheetNoAndHeadRowNumber();
                sheetNoAndHeadRowNumber = sheetNoAndHeadRowNumber == null ? ImmutableMap.of() : sheetNoAndHeadRowNumber;
                Integer headRowNumber = sheetNoAndHeadRowNumber.get(sheetNo);
                AbstractExcelBaseListener<?> listener = sheetNoAndListener.get(sheetNo);
                if (listener == null) {
                    throw new RuntimeException("excel解析器不能为null");
                }
                log.info("--> sheetName : {}", sheetName);
                excelReader.read(
                        EasyExcel.readSheet(sheetNo, sheetName)
                                .headRowNumber(headRowNumber)
                                .registerReadListener(listener)
                                .autoTrim(true)
                                .build()
                );
                List<?> dataList = listener.getDataList();
                List<JSONObject> jsonObjectList = dataList.stream().map(o -> JSON.parseObject(JSONObject.toJSONString(o))).collect(Collectors.toList());
                log.info("--> Put data:{}", JSONObject.toJSONString(jsonObjectList));
                result.put(sheetNo, jsonObjectList);
            });
            excelReader.finish();
            return result;
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Maps.newConcurrentMap();
    }

    /**
     * description: 生成excel无指定模板-简单表头-单个sheet
     *
     * @param writeFreedomParam 无模板excel生成入参
     * @return boolean true：生成成功 false：生成失败
     * @author k
     * @date 2020/05/23 10:40
     **/
    public static boolean writeSimpleExcel(WriteFreedomParam writeFreedomParam) {
        String filePath = writeFreedomParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            String sheetName = writeFreedomParam.getSheetName();
            sheetName = StringUtils.isBlank(sheetName) ? "未定义" : sheetName;
            List<List<String>> data = writeFreedomParam.getData();
            List<String> simpleHead = writeFreedomParam.getSimpleHead();
            Boolean autoWidth = writeFreedomParam.getAutoWidth();
            autoWidth = autoWidth == null ? false : autoWidth;
            if (simpleHead == null) {
                throw new RuntimeException("单行表头不能为null");
            }
            if (data == null) {
                throw new RuntimeException("数据源不能为null");
            }
            List<List<String>> headList = Lists.newArrayList();
            simpleHead.forEach(h -> headList.add(Collections.singletonList(h)));
            writeNoModel(writeFreedomParam, outputStream, sheetName, data, autoWidth, headList);
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
     * description: 生成excel无指定模板-简单表头-单个sheet
     *
     * @param writeFreedomParam 无模板excel生成入参
     * @return boolean true：生成成功 false：生成失败
     * @author k
     * @date 2020/05/23 10:41
     **/
    public static boolean writeHardExcel(WriteFreedomParam writeFreedomParam) {
        String filePath = writeFreedomParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            String sheetName = writeFreedomParam.getSheetName();
            sheetName = StringUtils.isBlank(sheetName) ? "未定义" : sheetName;
            List<List<String>> hardHead = writeFreedomParam.getHardHead();
            List<List<String>> data = writeFreedomParam.getData();
            Boolean autoWidth = writeFreedomParam.getAutoWidth();
            autoWidth = autoWidth == null ? false : autoWidth;
            if (hardHead == null) {
                throw new RuntimeException("多行表头不能为null");
            }
            if (data == null) {
                throw new RuntimeException("数据源不能为null");
            }
            writeNoModel(writeFreedomParam, outputStream, sheetName, data, autoWidth, hardHead);
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

    private static void writeNoModel(WriteFreedomParam writeFreedomParam, OutputStream outputStream, String sheetName, List<List<String>> data, Boolean autoWidth, List<List<String>> headList) {
        ExcelWriterBuilder write = EasyExcel.write(outputStream);
        if (writeFreedomParam.getCellBaseHandler() != null) {
            write.registerWriteHandler(writeFreedomParam.getCellBaseHandler());
        }
        if (writeFreedomParam.getSheetBaseHandler() != null) {
            write.registerWriteHandler(writeFreedomParam.getSheetBaseHandler());
        }
        if (autoWidth) {
            write.registerWriteHandler(new ColumnWidthStyleStrategy());
        }
        write.useDefaultStyle(false)
                .needHead(true)
                .head(headList)
                .sheet(sheetName)
                .doWrite(data);
    }


    /**
     * description: 生成多Sheet的excel-简单单行表头-无模板
     *
     * @param writeFreedomMultipleParam 多sheet自由模板入参
     * @return boolean  true：生成成功 false：生成失败
     * @author k
     * @date 2020/05/23 13:51
     **/
    public static boolean writeWithMultipleSheetNoModelSimple(WriteFreedomMultipleParam writeFreedomMultipleParam) {
        String filePath = writeFreedomMultipleParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            List<MultipleFreedomSheetProperty> multipleFreedomSheetProperties = writeFreedomMultipleParam.getMultipleFreedomSheetProperties();
            if (multipleFreedomSheetProperties == null) {
                throw new RuntimeException("多sheet的数据源不能为null");
            }
            AtomicInteger index = new AtomicInteger();
            ExcelWriter excelWriter = EasyExcel.write(outputStream)
                    .needHead(true)
                    .useDefaultStyle(false)
                    .autoCloseStream(false)
                    .build();
            multipleFreedomSheetProperties.forEach(multipleFreedomSheetProperty -> {
                String sheetName = multipleFreedomSheetProperty.getSheetName();
                sheetName = StringUtils.isBlank(sheetName) ? "未定义" + index.get() : sheetName;
                List<List<?>> data = multipleFreedomSheetProperty.getData();
                List<String> simpleHead = multipleFreedomSheetProperty.getSimpleHead();
                Boolean autoWidth = multipleFreedomSheetProperty.getAutoWidth();
                autoWidth = autoWidth == null ? false : autoWidth;
                if (simpleHead == null) {
                    throw new RuntimeException("单行表头不能为null");
                }
                if (data == null) {
                    throw new RuntimeException("数据源不能为null");
                }
                List<List<String>> headList = Lists.newArrayList();
                simpleHead.forEach(h -> headList.add(Collections.singletonList(h)));
                ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet(index.get(), sheetName)
                        .head(headList);
                if (multipleFreedomSheetProperty.getCellBaseHandler() != null) {
                    excelWriterSheetBuilder.registerWriteHandler(multipleFreedomSheetProperty.getCellBaseHandler());
                }
                if (multipleFreedomSheetProperty.getSheetBaseHandler() != null) {
                    excelWriterSheetBuilder.registerWriteHandler(multipleFreedomSheetProperty.getSheetBaseHandler());
                }
                if (autoWidth) {
                    excelWriterSheetBuilder.registerWriteHandler(new ColumnWidthStyleStrategy());
                }
                WriteSheet writeSheet = excelWriterSheetBuilder.build();
                excelWriter.write(data, writeSheet);
                index.getAndIncrement();
            });
            excelWriter.finish();
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
     * description: 生成多Sheet的excel-复制多行表头-无模板
     *
     * @param writeFreedomMultipleParam 多sheet自由模板入参
     * @return boolean  true：生成成功 false：生成失败
     * @author k
     * @date 2020/05/23 13:51
     **/
    public static boolean writeWithMultipleSheetNoModelHard(WriteFreedomMultipleParam writeFreedomMultipleParam) {
        String filePath = writeFreedomMultipleParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            List<MultipleFreedomSheetProperty> multipleFreedomSheetProperties = writeFreedomMultipleParam.getMultipleFreedomSheetProperties();
            if (multipleFreedomSheetProperties == null) {
                throw new RuntimeException("多sheet的数据源不能为null");
            }
            AtomicInteger index = new AtomicInteger();
            ExcelWriter excelWriter = EasyExcel.write(outputStream)
                    .needHead(true)
                    .useDefaultStyle(false)
                    .autoCloseStream(false)
                    .build();
            multipleFreedomSheetProperties.forEach(multipleFreedomSheetProperty -> {
                String sheetName = multipleFreedomSheetProperty.getSheetName();
                sheetName = StringUtils.isBlank(sheetName) ? "未定义" + index.get() : sheetName;
                List<List<?>> data = multipleFreedomSheetProperty.getData();
                List<List<String>> hardHead = multipleFreedomSheetProperty.getHardHead();
                Boolean autoWidth = multipleFreedomSheetProperty.getAutoWidth();
                autoWidth = autoWidth == null ? false : autoWidth;
                if (hardHead == null) {
                    throw new RuntimeException("单行表头不能为null");
                }
                if (data == null) {
                    throw new RuntimeException("数据源不能为null");
                }
                ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet(index.get(), sheetName)
                        .head(hardHead);
                if (multipleFreedomSheetProperty.getCellBaseHandler() != null) {
                    excelWriterSheetBuilder.registerWriteHandler(multipleFreedomSheetProperty.getCellBaseHandler());
                }
                if (multipleFreedomSheetProperty.getSheetBaseHandler() != null) {
                    excelWriterSheetBuilder.registerWriteHandler(multipleFreedomSheetProperty.getSheetBaseHandler());
                }
                if (autoWidth) {
                    excelWriterSheetBuilder.registerWriteHandler(new ColumnWidthStyleStrategy());
                }
                WriteSheet writeSheet = excelWriterSheetBuilder.build();
                excelWriter.write(data, writeSheet);
                index.getAndIncrement();
            });
            excelWriter.finish();
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


}
