package com.fans.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.fans.utils.excel.base.AbstractExcelBaseListener;
import com.fans.utils.excel.param.read.ReadExcelParam;
import com.fans.utils.excel.param.read.ReadMultipleSheetParam;
import com.fans.utils.excel.param.write.MultipleSheetProperty;
import com.fans.utils.excel.param.write.WriteExcelMultipleParam;
import com.fans.utils.excel.param.write.WriteExcelParam;
import com.fans.utils.excel.strategy.ColumnWidthStyleStrategy;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * className: NewExcelUtils
 *
 * @author k
 * @version 1.0
 * @description 新版excel渲染
 * @date 2020-05-18 23:02
 **/
@Slf4j
public class ExcelUtils {

    /**
     * description: 有模板,读小于1000行数据, 带样式
     *
     * @param readExcelParam 单模板单sheet读取通用入参
     * @return java.util.List<T>
     * @author k
     * @date 2020/05/19 0:13
     **/
    public static <T> List<T> readLessThan1000RowByModel(ReadExcelParam<T> readExcelParam) {
        int sheetNo = readExcelParam.getSheetNo();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        Class<T> model = readExcelParam.getModel();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空!!!");
        }
        if (!Optional.ofNullable(model).isPresent()) {
            throw new RuntimeException("excel模板为空!!!");
        }
        try (InputStream inputStream = new FileInputStream(filePath)) {

            return EasyExcel.read(inputStream)
                    .head(model)
                    .sheet(sheetNo)
                    .headRowNumber(headRowNumber)
                    .autoTrim(true)
                    .doReadSync();

        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * description: 有模板,读大于1000行数据, 带样式
     *
     * @param readExcelParam 单模板单sheet读取通用入参
     * @return java.util.List<T>
     * @author k
     * @date 2020/05/19 0:25
     **/
    public static <T> List<T> readMoreThan1000RowByModel(ReadExcelParam<T> readExcelParam) {
        int sheetNo = readExcelParam.getSheetNo();
        int headRowNumber = readExcelParam.getHeadRowNumber();
        String filePath = readExcelParam.getFilePath();
        Class<T> model = readExcelParam.getModel();
        AbstractExcelBaseListener<T> listener = readExcelParam.getListener();
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
            EasyExcel.read(fileStream, model, listener)
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
     * description: 有模板,读多个sheet的数据, 带样式
     *
     * @param readMultipleSheetParam 多模板多sheet读取通用入参
     * @return java.util.Map<java.lang.Integer, java.util.List < T>>
     * @author k
     * @date 2020/05/19 1:07
     **/
    public static Map<Integer, List<?>> readMoreSheetByModel(ReadMultipleSheetParam readMultipleSheetParam) {
        String filePath = readMultipleSheetParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        Map<Integer, List<?>> result = Maps.newHashMap();
        try (InputStream fileStream = new FileInputStream(filePath)) {
            ExcelReader excelReader = EasyExcel.read(fileStream).build();
            List<ReadSheet> readSheets = excelReader.excelExecutor().sheetList();
            readSheets.forEach(readSheet -> {
                String sheetName = readSheet.getSheetName();
                Integer sheetNo = readSheet.getSheetNo();
                ImmutableMap<Integer, Integer> sheetNoAndHeadRowNumber = readMultipleSheetParam.getSheetNoAndHeadRowNumber();
                sheetNoAndHeadRowNumber = sheetNoAndHeadRowNumber == null ? ImmutableMap.of() : sheetNoAndHeadRowNumber;
                ImmutableMap<Integer, Class<?>> sheetNoAndModel = readMultipleSheetParam.getSheetNoAndModel();
                sheetNoAndModel = sheetNoAndModel == null ? ImmutableMap.of() : sheetNoAndModel;
                ImmutableMap<Integer, AbstractExcelBaseListener<?>> sheetNoAndListener = readMultipleSheetParam.getSheetNoAndListener();
                sheetNoAndListener = sheetNoAndListener == null ? ImmutableMap.of() : sheetNoAndListener;
                Integer headRowNumber = sheetNoAndHeadRowNumber.get(sheetNo);
                headRowNumber = headRowNumber == null ? 0 : headRowNumber;
                Class<?> model = sheetNoAndModel.get(sheetNo);
                AbstractExcelBaseListener<?> listener = sheetNoAndListener.get(sheetNo);
                if (model == null) {
                    throw new RuntimeException("excel模板不能为null");
                }
                if (listener == null) {
                    throw new RuntimeException("excel解析器不能为null");
                }
                excelReader.read(
                        EasyExcel.readSheet(sheetNo, sheetName)
                                .head(model)
                                .registerReadListener(listener)
                                .headRowNumber(headRowNumber)
                                .autoTrim(true)
                                .build()
                );
                List<?> dataList = listener.getDataList();
                log.info("--> sheetName : {}", sheetName);
                log.info("--> Put data:{}", JSONObject.toJSONString(dataList));
                //读取的数据放入map key为sheetNo
                result.put(sheetNo, dataList);
            });
            excelReader.finish();
            return result;
        } catch (FileNotFoundException e) {
            log.error("--> 1、找不到文件2、文件路径错误3、文件资源被占用, 文件：{}", filePath);
        } catch (IOException e) {
            log.error("--> excel文件读取失败, 失败原因：{}", e.getMessage(), e);
        }
        return Maps.newHashMap();
    }

    public static <T> boolean writeExcelByModel(WriteExcelParam<T> writeExcelParam) {
        String filePath = writeExcelParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            String sheetName = writeExcelParam.getSheetName();
            sheetName = StringUtils.isBlank(sheetName) ? "未定义" : sheetName;
            List<T> modelData = writeExcelParam.getModelData();
            Class<T> model = writeExcelParam.getModel();
            Boolean autoWidth = writeExcelParam.getAutoWidth();
            autoWidth = autoWidth == null ? false : autoWidth;
            if (modelData == null) {
                throw new RuntimeException("数据源不能为null");
            }
            if (model == null) {
                throw new RuntimeException("模板不能为null");
            }
            ExcelWriterBuilder write = EasyExcel.write(outputStream, model);
            if (writeExcelParam.getCellBaseHandler() != null) {
                write.registerWriteHandler(writeExcelParam.getCellBaseHandler());
            }
            if (writeExcelParam.getSheetBaseHandler() != null) {
                write.registerWriteHandler(writeExcelParam.getSheetBaseHandler());
            }
            if (autoWidth) {
                write.registerWriteHandler(new ColumnWidthStyleStrategy());
            }
            write.needHead(true)
                    .useDefaultStyle(false)
                    .sheet(sheetName)
                    .doWrite(modelData);
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

    public static boolean writeWithMultipleSheet(WriteExcelMultipleParam writeExcelMultipleParam) {
        String filePath = writeExcelMultipleParam.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件绝对路径不能为空");
        }
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            List<MultipleSheetProperty<?>> multipleSheetProperties = writeExcelMultipleParam.getMultipleSheetProperties();
            if (multipleSheetProperties == null) {
                throw new RuntimeException("多sheet的数据源不能为null");
            }
            AtomicInteger index = new AtomicInteger();
            ExcelWriter excelWriter = EasyExcel.write(outputStream)
                    .needHead(true)
                    .useDefaultStyle(false)
                    .autoCloseStream(false)
                    .build();
            multipleSheetProperties.forEach(multipleSheetProperty -> {
                String sheetName = multipleSheetProperty.getSheetName();
                sheetName = StringUtils.isBlank(sheetName) ? "未定义" + index.get() : sheetName;
                Class<?> model = multipleSheetProperty.getModel();
                List<?> modelData = multipleSheetProperty.getModelData();
                Boolean autoWidth = multipleSheetProperty.getAutoWidth();
                autoWidth = autoWidth == null ? false : autoWidth;
                if (modelData == null) {
                    throw new RuntimeException("数据源不能为null");
                }
                if (model == null) {
                    throw new RuntimeException("模板不能为null");
                }
                ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet().head(model);
                if (multipleSheetProperty.getCellBaseHandler() != null) {
                    excelWriterSheetBuilder.registerWriteHandler(multipleSheetProperty.getCellBaseHandler());
                }
                if (multipleSheetProperty.getSheetBaseHandler() != null) {
                    excelWriterSheetBuilder.registerWriteHandler(multipleSheetProperty.getSheetBaseHandler());
                }
                if (autoWidth) {
                    excelWriterSheetBuilder.registerWriteHandler(new ColumnWidthStyleStrategy());
                }
                WriteSheet writeSheet = excelWriterSheetBuilder.needHead(true)
                        .sheetName(sheetName)
                        .sheetNo(index.get())
                        .build();
                excelWriter.write(modelData, writeSheet);
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


    private static OutputStream getOutputStream(String fileName, HttpServletResponse response, HttpServletRequest request) {
        try {
            response.reset();
            response.setContentType("multipart/form-data");
            fileName = fileName + ".xlsx";
            if (StringUtils.isNotBlank(fileName)) {
                String headerName = "User-Agent";
                if (request.getHeader(headerName).toLowerCase().indexOf("firefox") > 0) {
                    // firefox浏览器
                    fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                } else if (request.getHeader(headerName).toUpperCase().indexOf("MSIE") > 0) {
                    // IE浏览器
                    fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
                } else if (request.getHeader(headerName).toUpperCase().indexOf("CHROME") > 0) {
                    // 谷歌
                    fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                }
            }
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            return response.getOutputStream();
        } catch (IOException e) {
            log.error("--> 获取输出流失败：{}", e.getMessage(), e);
        }
        return null;
    }
}
