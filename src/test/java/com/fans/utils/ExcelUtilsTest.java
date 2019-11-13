package com.fans.utils;

import com.alibaba.fastjson.JSONObject;
import com.fans.utils.excel.ExcelUtils;
import com.fans.utils.excel.handler.CustomCellWriteHandler;
import com.fans.utils.excel.handler.CustomSheetWriteHandler;
import com.fans.utils.excel.listener.ExcelListener;
import com.fans.utils.excel.model.TableHeaderExcelProperty;
import com.fans.utils.excel.param.MultipleSheetProperty;
import com.fans.utils.excel.param.ReadExcelParam;
import com.fans.utils.excel.param.WriteExcelParam;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ExcelUtils
 * @Description: TODO excel工具类测试
 * @Author k
 * @Date 2019-11-13 13:36
 * @Version 1.0
 **/
public class ExcelUtilsTest {

    @Test
    public void writeExcelByModel() {
        String filePath = "D:\\test1.xls";
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
            tableHeaderExcelProperty.setCell8("大师傅8");
            tableHeaderExcelProperty.setCell9("大师傅9");
            tableHeaderExcelProperty.setCell10("大师傅20");
            data.add(tableHeaderExcelProperty);
        }
        WriteExcelParam<TableHeaderExcelProperty> writeExcelParam = WriteExcelParam.<TableHeaderExcelProperty>builder().
                filePath(filePath)
                .sheetName("自定义")
                .modelData(data)
                .model(TableHeaderExcelProperty.class)
                .build();
        ExcelUtils.writeExcelByModel(writeExcelParam);
    }

    @Test
    public void readLessThan1000Row() {
        String filePath = "D:\\test1.xls";
        ReadExcelParam readExcelParam = ReadExcelParam.builder()
                .sheetNo(0)
                .headRowNumber(0)
                .filePath(filePath)
                .build();
        List<JSONObject> objects = ExcelUtils.readLessThan1000Row(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(objects));
    }

    @Test
    public void readLessThan1000RowByModel() {
        String filePath = "D:\\test1.xls";
        ReadExcelParam<TableHeaderExcelProperty> readExcelParam = ReadExcelParam.<TableHeaderExcelProperty>builder()
                .sheetNo(0)
                .headRowNumber(0)
                .filePath(filePath)
                .model(TableHeaderExcelProperty.class)
                .build();
        List<TableHeaderExcelProperty> objects = ExcelUtils.readLessThan1000RowByModel(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(objects));
    }

    @Test
    public void readMoreThan1000RowByModel() {
        String filePath = "D:\\test1.xls";
        ReadExcelParam<TableHeaderExcelProperty> readExcelParam = ReadExcelParam.<TableHeaderExcelProperty>builder()
                .sheetNo(0)
                .headRowNumber(0)
                .filePath(filePath)
                .model(TableHeaderExcelProperty.class)
                .listener(ExcelListener.class)
                .build();
        List<TableHeaderExcelProperty> objects = ExcelUtils.readMoreThan1000RowByModel(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(objects));
    }

    @Test
    public void readMoreThan1000Row() {
        String filePath = "D:\\test1.xls";
        ReadExcelParam readExcelParam = ReadExcelParam.builder()
                .sheetNo(0)
                .headRowNumber(0)
                .filePath(filePath)
                .listener(ExcelListener.class)
                .build();
        List<JSONObject> objects = ExcelUtils.readMoreThan1000Row(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(objects));
    }

    @Test
    public void readMoreSheetByModel() {
        String filePath = "D:\\test1.xls";
        ReadExcelParam<TableHeaderExcelProperty> readExcelParam = ReadExcelParam.<TableHeaderExcelProperty>builder()
                .headRowNumber(0)
                .filePath(filePath)
                .model(TableHeaderExcelProperty.class)
                .build();
        Map<Integer, List<TableHeaderExcelProperty>> objects = ExcelUtils.readMoreSheetByModel(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(objects));
    }

    @Test
    public void readMoreSheet() {
        String filePath = "D:\\test1.xls";
        ReadExcelParam readExcelParam = ReadExcelParam.builder()
                .headRowNumber(0)
                .filePath(filePath)
                .build();
        Map<Integer, List<JSONObject>> objects = ExcelUtils.readMoreSheet(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(objects));
    }

    @Test
    public void writeSimpleExcel() {
        String filePath = "D:\\test1.xls";
        List<List<String>> data = Lists.newArrayList();
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        List<String> simpleHead = Lists.newArrayList("表头1", "表头2", "表头3", "表头4", "表头5");
        WriteExcelParam<Object> writeExcelParam = WriteExcelParam.builder()
                .filePath(filePath)
                .sheetName("自定义")
                .simpleHead(simpleHead)
                .data(data)
                .build();
        ExcelUtils.writeSimpleExcel(writeExcelParam);
    }

    @Test
    public void writeHardExcel() {
        String filePath = "D:\\test1.xls";
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
        WriteExcelParam<Object> writeExcelParam = WriteExcelParam.builder()
                .filePath(filePath)
                .sheetName("自定义")
                .hardHead(headList)
                .data(data)
                .build();
        ExcelUtils.writeHardExcel(writeExcelParam);
    }

    @Test
    public void writeWithMultipleSheet() {
        String filePath = "D:\\test1.xls";
        List<TableHeaderExcelProperty> rowList1 = Lists.newArrayList();
        TableHeaderExcelProperty cell1 = new TableHeaderExcelProperty();
        cell1.setCell1("大师傅1");
        cell1.setCell2("大师傅2");
        cell1.setCell3("大师傅3");
        cell1.setCell4("大师傅4");
        cell1.setCell5("大师傅5");
        cell1.setCell6("大师傅6");
        cell1.setCell7("大师傅7");
        cell1.setCell8("大师傅8");
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
        cell.setCell8("大师傅81");
        cell.setCell9("大师傅91");
        cell.setCell10("大师傅10");
        rowList2.add(cell);
        List<MultipleSheetProperty<TableHeaderExcelProperty>> multipleSheetProperties = Lists.newArrayList(
                MultipleSheetProperty.<TableHeaderExcelProperty>builder()
                        .sheetName("111")
                        .data(rowList1)
                        .build(),
                MultipleSheetProperty.<TableHeaderExcelProperty>builder()
                        .sheetName("222")
                        .data(rowList2)
                        .build()
        );
        WriteExcelParam<TableHeaderExcelProperty> writeExcelParam = WriteExcelParam.<TableHeaderExcelProperty>builder()
                .filePath(filePath)
                .multipleSheetProperties(multipleSheetProperties)
                .model(TableHeaderExcelProperty.class)
                .sheetWriteHandler(CustomSheetWriteHandler.class)
                .build();
        ExcelUtils.writeWithMultipleSheet(writeExcelParam);
    }

    @Test
    public void writeWithMultipleSheetNoModelSimple() {
        String filePath = "D:\\test1.xls";
        List<List<Object>> data = Lists.newArrayList();
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        data.add(Lists.newArrayList("111", "222", "333", "444", "555"));
        List<String> simpleHead = Lists.newArrayList("表头1", "表头2", "表头3", "表头4", "表头5");
        List<MultipleSheetProperty<List<Object>>> multipleSheetPropertyList = Lists.newArrayList();
        multipleSheetPropertyList.add(MultipleSheetProperty.<List<Object>>builder()
                .sheetName("111")
                .data(data)
                .build());
        multipleSheetPropertyList.add(MultipleSheetProperty.<List<Object>>builder()
                .sheetName("222")
                .data(data)
                .build());
        WriteExcelParam<List<Object>> writeExcelParam = WriteExcelParam.<List<Object>>builder()
                .filePath(filePath)
                .simpleHead(simpleHead)
                .multipleSheetProperties(multipleSheetPropertyList)
                .cellWriteHandler(CustomCellWriteHandler.class)
                .build();
        ExcelUtils.writeWithMultipleSheetNoModelSimple(writeExcelParam);
    }

    @Test
    public void writeWithMultipleSheetNoModelHard() {
        String filePath = "D:\\test1.xls";
        List<List<Object>> data = Lists.newArrayList();
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
        List<MultipleSheetProperty<List<Object>>> multipleSheetPropertyList = Lists.newArrayList();
        multipleSheetPropertyList.add(MultipleSheetProperty.<List<Object>>builder()
                .sheetName("111")
                .data(data)
                .build());
        multipleSheetPropertyList.add(MultipleSheetProperty.<List<Object>>builder()
                .sheetName("222")
                .data(data)
                .build());
        WriteExcelParam<List<Object>> writeExcelParam = WriteExcelParam.<List<Object>>builder()
                .filePath(filePath)
                .hardHead(headList)
                .multipleSheetProperties(multipleSheetPropertyList)
                .build();
        ExcelUtils.writeWithMultipleSheetNoModelHard(writeExcelParam);
    }
}
