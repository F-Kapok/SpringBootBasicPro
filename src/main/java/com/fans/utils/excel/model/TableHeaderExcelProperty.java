package com.fans.utils.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName TableHeaderExcelProperty
 * @Description: 模板案例  测试用
 * @Author k
 * @Date 2019-11-11 15:11
 * @Version 1.0
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
    @ExcelProperty(value = "第八列")
    private String cell8;
    @ExcelProperty(value = "第九列")
    private String cell9;
    @ExcelProperty(value = "第十列")
    private String cell10;
}
