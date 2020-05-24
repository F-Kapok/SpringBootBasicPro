package com.fans.utils;

import com.alibaba.fastjson.JSONObject;
import com.fans.utils.excel.NoModelExcelLocalUtils;
import com.fans.utils.excel.base.SheetBaseHandler;
import com.fans.utils.excel.handler.CustomCellWriteHandler;
import com.fans.utils.excel.handler.CustomSheetWriteHandler;
import com.fans.utils.excel.listener.ExcelListener;
import com.fans.utils.excel.model.TableHeaderExcelProperty;
import com.fans.utils.excel.param.read.ReadExcelParam;
import com.fans.utils.excel.param.read.ReadMultipleSheetParam;
import com.fans.utils.excel.param.write.MultipleFreedomSheetProperty;
import com.fans.utils.excel.param.write.WriteFreedomMultipleParam;
import com.fans.utils.excel.param.write.WriteFreedomParam;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * className: NoModelExcelUtilsTest
 *
 * @author k
 * @version 1.0
 * @description
 * @date 2020-05-19 00:00
 **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class NoModelExcelUtilsTest {


    @Test
    public void writeSimpleExcel() {
        String filePath = "D:\\test1.xlsx";
        List<List<String>> data = Lists.newArrayList();
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        List<String> simpleHead = Lists.newArrayList("表头1", "表头2", "表头3", "表头4", "表头5");
        WriteFreedomParam writeFreedomParam = WriteFreedomParam.builder()
                .filePath(filePath)
                .sheetName("自定义")
                .simpleHead(simpleHead)
                .autoWidth(true)
                .cellBaseHandler(new CustomCellWriteHandler())
                .sheetBaseHandler(new SheetBaseHandler())
                .data(data)
                .build();
        NoModelExcelLocalUtils.writeSimpleExcel(writeFreedomParam);
    }

    @Test
    public void writeHardExcel() {
        String filePath = "D:\\test1.xlsx";
        List<List<String>> data = Lists.newArrayList();
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        List<String> head1 = Lists.newArrayList("表头1");
        List<String> head2 = Lists.newArrayList("表头1", "表头2");
        List<String> head3 = Lists.newArrayList("表头1", "表头3");
        List<String> head4 = Lists.newArrayList("表头4", "表头5");
        List<String> head5 = Lists.newArrayList("表头6");
        List<List<String>> headList = Lists.newArrayList();
        headList.add(head1);
        headList.add(head2);
        headList.add(head3);
        headList.add(head4);
        headList.add(head5);
        WriteFreedomParam writeFreedomParam = WriteFreedomParam.builder()
                .filePath(filePath)
                .sheetName("自定义")
                .hardHead(headList)
                .autoWidth(true)
                .cellBaseHandler(new CustomCellWriteHandler())
                .sheetBaseHandler(new SheetBaseHandler())
                .data(data)
                .build();
        NoModelExcelLocalUtils.writeHardExcel(writeFreedomParam);
    }

    @Test
    public void readLessThan1000Row() {
        String filePath = "D:\\test1.xlsx";
        ReadExcelParam<JSONObject> readExcelParam = ReadExcelParam.<JSONObject>builder()
                .filePath(filePath)
                .model(JSONObject.class)
                .build();
        List<JSONObject> tableHeaderExcelProperties = NoModelExcelLocalUtils.readLessThan1000Row(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(tableHeaderExcelProperties));
    }

    @Test
    public void readMoreThan1000Row() {
        String filePath = "D:\\test1.xlsx";
        ReadExcelParam<JSONObject> readExcelParam = ReadExcelParam.<JSONObject>builder()
                .sheetNo(0)
                .headRowNumber(0)
                .filePath(filePath)
                .listener(new ExcelListener<>())
                .build();
        List<JSONObject> objects = NoModelExcelLocalUtils.readMoreThan1000Row(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(objects));
    }

    @Test
    public void writeWithMultipleSheetNoModelSimple() {
        String filePath = "D:\\test1.xlsx";
        List<List<?>> data = Lists.newArrayList();
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        List<String> simpleHead = Lists.newArrayList("表头1", "表头2", "表头3", "表头4", "表头5");
        List<MultipleFreedomSheetProperty> multipleFreedomSheetPropertyList = Lists.newArrayList();
        multipleFreedomSheetPropertyList.add(MultipleFreedomSheetProperty.builder()
                .sheetName("111")
                .simpleHead(simpleHead)
                .autoWidth(true)
                .cellBaseHandler(new CustomCellWriteHandler())
                .sheetBaseHandler(new SheetBaseHandler())
                .data(data)
                .build());
        multipleFreedomSheetPropertyList.add(MultipleFreedomSheetProperty.builder()
                .simpleHead(simpleHead)
                .sheetName("222")
                .data(data)
                .build());
        WriteFreedomMultipleParam writeFreedomMultipleParam = WriteFreedomMultipleParam.builder()
                .filePath(filePath)
                .multipleFreedomSheetProperties(multipleFreedomSheetPropertyList)
                .build();
        NoModelExcelLocalUtils.writeWithMultipleSheetNoModelSimple(writeFreedomMultipleParam);
    }

    @Test
    public void writeWithMultipleSheetNoModelHard() {
        String filePath = "D:\\test1.xlsx";
        List<List<?>> data = Lists.newArrayList();
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        List<String> head1 = Lists.newArrayList("表头1");
        List<String> head2 = Lists.newArrayList("表头1", "表头2");
        List<String> head3 = Lists.newArrayList("表头1", "表头3");
        List<String> head4 = Lists.newArrayList("表头4", "表头5");
        List<String> head5 = Lists.newArrayList("表头6");
        List<List<String>> headList = Lists.newArrayList();
        headList.add(head1);
        headList.add(head2);
        headList.add(head3);
        headList.add(head4);
        headList.add(head5);
        List<MultipleFreedomSheetProperty> multipleFreedomSheetPropertyList = Lists.newArrayList();
        multipleFreedomSheetPropertyList.add(MultipleFreedomSheetProperty.builder()
                .hardHead(headList)
                .sheetName("111")
                .data(data)
                .build());
        multipleFreedomSheetPropertyList.add(MultipleFreedomSheetProperty.builder()
                .hardHead(headList)
                .sheetName("222")
                .cellBaseHandler(new CustomCellWriteHandler())
                .autoWidth(true)
                .sheetBaseHandler(new CustomSheetWriteHandler())
                .data(data)
                .build());
        WriteFreedomMultipleParam writeFreedomMultipleParam = WriteFreedomMultipleParam.builder()
                .filePath(filePath)
                .multipleFreedomSheetProperties(multipleFreedomSheetPropertyList)
                .build();
        NoModelExcelLocalUtils.writeWithMultipleSheetNoModelHard(writeFreedomMultipleParam);
    }

    @Test
    public void readMoreSheet() {
        String filePath = "D:\\test1.xlsx";
        ReadMultipleSheetParam readMultipleSheetParam = ReadMultipleSheetParam.builder()
                .filePath(filePath)
                .sheetNoAndHeadRowNumber(ImmutableMap.of(0, 0, 1, 0))
                .sheetNoAndListener(ImmutableMap.of(0, new ExcelListener<TableHeaderExcelProperty>(), 1, new ExcelListener<TableHeaderExcelProperty>()))
                .build();
        Map<Integer, List<JSONObject>> result = NoModelExcelLocalUtils.readMoreSheet(readMultipleSheetParam);
        System.out.println(JsonUtils.obj2FormattingString(result));
    }


}
