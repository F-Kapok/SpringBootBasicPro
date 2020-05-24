package com.fans.utils;

import com.fans.utils.excel.ExcelUtils;
import com.fans.utils.excel.base.SheetBaseHandler;
import com.fans.utils.excel.handler.CustomCellWriteHandler;
import com.fans.utils.excel.handler.CustomSheetWriteHandler;
import com.fans.utils.excel.listener.ExcelListener;
import com.fans.utils.excel.model.TableHeaderExcelProperty;
import com.fans.utils.excel.param.read.ReadExcelParam;
import com.fans.utils.excel.param.read.ReadMultipleSheetParam;
import com.fans.utils.excel.param.write.MultipleSheetProperty;
import com.fans.utils.excel.param.write.WriteExcelMultipleParam;
import com.fans.utils.excel.param.write.WriteExcelParam;
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
                .modelData(Lists.newArrayList())
                .cellBaseHandler(new CustomCellWriteHandler())
                .sheetBaseHandler(new CustomSheetWriteHandler())
                .model(TableHeaderExcelProperty.class)
                .build();
        ExcelUtils.writeExcelByModel(writeExcelParam);
    }


    @Test
    public void readLessThan1000RowByModel() {
        String filePath = "D:\\test1.xlsx";
        ReadExcelParam<TableHeaderExcelProperty> readExcelParam = ReadExcelParam.<TableHeaderExcelProperty>builder()
                .filePath(filePath)
                .model(TableHeaderExcelProperty.class)
                .build();
        List<TableHeaderExcelProperty> tableHeaderExcelProperties = ExcelUtils.readLessThan1000RowByModel(readExcelParam);
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
        List<TableHeaderExcelProperty> tableHeaderExcelProperties = ExcelUtils.readMoreThan1000RowByModel(readExcelParam);
        System.out.println(JsonUtils.obj2FormattingString(tableHeaderExcelProperties));
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
        ExcelUtils.writeWithMultipleSheet(excelMultipleParam);
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
        Map<Integer, List<?>> result = ExcelUtils.readMoreSheetByModel(readMultipleSheetParam);
        System.out.println(JsonUtils.obj2FormattingString(result));
    }

}
