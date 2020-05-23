package com.fans.utils;

import com.alibaba.fastjson.JSONObject;
import com.fans.utils.excel.ExcelLocalUtils;
import com.fans.utils.excel.NoModelExcelLocalUtils;
import com.fans.utils.excel.base.SheetBaseHandler;
import com.fans.utils.excel.handler.CustomCellWriteHandler;
import com.fans.utils.excel.handler.CustomSheetWriteHandler;
import com.fans.utils.excel.listener.ExcelListener;
import com.fans.utils.excel.model.TableHeaderExcelProperty;
import com.fans.utils.excel.param.read.ReadExcelParam;
import com.fans.utils.excel.param.read.ReadMultipleSheetParam;
import com.fans.utils.excel.param.write.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * className: LocalExcelUtilsTest
 *
 * @author k
 * @version 1.0
 * @description
 * @date 2020-05-19 00:00
 **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ExcelUtilsTest {


    @Test
    public void writeExcelByModel() {
        String filePath = "D:\\test1.xlsx";
        List<TableHeaderExcelProperty> data = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            TableHeaderExcelProperty tableHeaderExcelProperty = new TableHeaderExcelProperty();
            tableHeaderExcelProperty.setCell1("大师傅1");
            tableHeaderExcelProperty.setCell2("大师傅2");
            tableHeaderExcelProperty.setCell3("大师傅3");
            tableHeaderExcelProperty.setCell4("大师傅4");
            tableHeaderExcelProperty.setCell5("大师傅5");
            tableHeaderExcelProperty.setCell6("大师傅6");
            tableHeaderExcelProperty.setCell7("大师傅7");
            tableHeaderExcelProperty.setCell8(new Date());
            tableHeaderExcelProperty.setCell9("1");
            tableHeaderExcelProperty.setCell10("2020/05/05");
            data.add(tableHeaderExcelProperty);
        }
        WriteExcelParam<TableHeaderExcelProperty> writeExcelParam = WriteExcelParam.<TableHeaderExcelProperty>builder().
                filePath(filePath)
                .sheetName("自定义")
                .autoWidth(true)
                .modelData(data)
                .cellBaseHandler(new CustomCellWriteHandler())
                .sheetBaseHandler(new SheetBaseHandler())
                .model(TableHeaderExcelProperty.class)
                .build();
        ExcelLocalUtils.writeExcelByModel(writeExcelParam);
    }


    @Test
    public void readLessThan1000RowByModel() {
        String filePath = "D:\\test1.xlsx";
        ReadExcelParam<TableHeaderExcelProperty> readExcelParam = ReadExcelParam.<TableHeaderExcelProperty>builder()
                .filePath(filePath)
                .model(TableHeaderExcelProperty.class)
                .build();
        List<TableHeaderExcelProperty> tableHeaderExcelProperties = ExcelLocalUtils.readLessThan1000RowByModel(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(tableHeaderExcelProperties));
    }

    @Test
    public void readMoreThan1000RowByModel() {
        String filePath = "D:\\test1.xlsx";
        ReadExcelParam<TableHeaderExcelProperty> readExcelParam = ReadExcelParam.<TableHeaderExcelProperty>builder()
                .sheetNo(0)
                .headRowNumber(0)
                .filePath(filePath)
                .listener(new ExcelListener<>())
                .model(TableHeaderExcelProperty.class)
                .build();
        List<TableHeaderExcelProperty> tableHeaderExcelProperties = ExcelLocalUtils.readMoreThan1000RowByModel(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(tableHeaderExcelProperties));
    }

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
    public void writeWithMultipleSheet() {
        String filePath = "D:\\test1.xlsx";
        List<TableHeaderExcelProperty> rowList1 = Lists.newArrayList();
        TableHeaderExcelProperty cell1 = new TableHeaderExcelProperty();
        cell1.setCell1("大师傅1");
        cell1.setCell2("大师傅2");
        cell1.setCell3("大师傅3");
        cell1.setCell4("大师傅4");
        cell1.setCell5("大师傅5");
        cell1.setCell6("大师傅6");
        cell1.setCell7("大师傅7");
        cell1.setCell8(new Date());
        cell1.setCell9("大师傅9");
        cell1.setCell10("大师傅20");
        rowList1.add(cell1);
        List<TableHeaderExcelProperty> rowList2 = Lists.newArrayList();
        TableHeaderExcelProperty cell = new TableHeaderExcelProperty();
        cell.setCell1("大师傅11");
        cell.setCell2("大师傅21");
        cell.setCell3("大师傅31");
        cell.setCell4("大师傅41");
        cell.setCell5("大师傅51");
        cell.setCell6("大师傅61");
        cell.setCell7("大师傅71");
        cell.setCell8(new Date());
        cell.setCell9("大师傅91");
        cell.setCell10("大师傅10");
        rowList2.add(cell);
        List<MultipleSheetProperty<?>> multipleSheetProperties = Lists.newArrayList(
                MultipleSheetProperty.<TableHeaderExcelProperty>builder()
                        .sheetName("111")
                        .modelData(rowList1)
                        .autoWidth(true)
                        .model(TableHeaderExcelProperty.class)
                        .build(),
                MultipleSheetProperty.<TableHeaderExcelProperty>builder()
                        .sheetName("222")
                        .modelData(rowList2)
                        .autoWidth(true)
                        .model(TableHeaderExcelProperty.class)
                        .cellBaseHandler(new CustomCellWriteHandler())
                        .sheetBaseHandler(new SheetBaseHandler())
                        .build()
        );
        WriteExcelMultipleParam excelMultipleParam = WriteExcelMultipleParam.builder()
                .filePath(filePath)
                .multipleSheetProperties(multipleSheetProperties)
                .build();
        ExcelLocalUtils.writeWithMultipleSheet(excelMultipleParam);
    }


    @Test
    public void readMoreSheetByModel() {
        String filePath = "D:\\test1.xlsx";
        ReadMultipleSheetParam readMultipleSheetParam = ReadMultipleSheetParam.builder()
                .filePath(filePath)
                .sheetNoAndHeadRowNumber(ImmutableMap.of(0, 0, 1, 0))
                .sheetNoAndModel(ImmutableMap.of(0, TableHeaderExcelProperty.class, 1, TableHeaderExcelProperty.class))
                .sheetNoAndListener(ImmutableMap.of(0, new ExcelListener<TableHeaderExcelProperty>(), 1, new ExcelListener<TableHeaderExcelProperty>()))
                .build();
        Map<Integer, List<?>> result = ExcelLocalUtils.readMoreSheetByModel(readMultipleSheetParam);
        System.out.println(JsonUtils.obj2FormattingString(result));
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
