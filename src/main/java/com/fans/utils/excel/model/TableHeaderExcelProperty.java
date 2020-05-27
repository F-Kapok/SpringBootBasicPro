package com.fans.utils.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.fans.utils.excel.convert.CustomStringDateConverter;
import lombok.Data;

import java.util.Date;

/**
 * className: TableHeaderExcelProperty
 *
 * @author k
 * @version 1.0
 * @description 模板案例  测试用
 * @date 2018-12-20 14:14
 **/
@Data
public class TableHeaderExcelProperty {
    @ExcelProperty(value = {"主标题", "第一列"})
    private String cell1;
    @ExcelProperty(value = {"主标题", "第二列"})
    private String cell2;
    @ExcelProperty(value = "第三列")
    private String cell3;
    @ExcelProperty(value = "第四列")
    private String cell4;
    @ExcelProperty(value = "第五列")
    private String cell5;
    @ExcelProperty(value = "第六列")
    private String cell6;
    @ExcelProperty(value = "第七列")
    private String cell7;
    @ExcelProperty(value = "第八列", converter = CustomStringDateConverter.class)
    private Date cell8;
    @ExcelProperty(value = "第九列")
    @NumberFormat(value = "#.##%")
    private String cell9;
    @ExcelProperty(value = "第十列")
    @DateTimeFormat(value = "yyyy年MM月dd日 HH时mm分ss秒")
    private String cell10;
}
