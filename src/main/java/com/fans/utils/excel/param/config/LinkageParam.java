package com.fans.utils.excel.param.config;

import lombok.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * className: LinkageParam
 *
 * @author k
 * @version 1.0
 * @description 联动设置入参
 * @date 2020-05-22 23:28
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LinkageParam implements Serializable {

    private static final long serialVersionUID = -20200522232858L;

    /**
     * 要操作的sheet
     */
    private Sheet currentSheet;
    /**
     * sheet工作
     */
    private Workbook workbook;
    /**
     * 联动信息sheet名称 例：省市区联动
     */
    private String hideSheetName;
    /**
     * 联动最高级数据  例：省市区，最高级是省的数据
     */
    private String[] parent;
    /**
     * 除了最后一级的信息没有key 其他的key必须对应父级  例：省市区县，  map中应该存  省:市： 市:区； 区:县。。。。以此类推
     */
    private LinkedHashMap<String, String[]> exceptLast;
    /**
     * 从第几行到第几行
     */
    private int firstRow;
    private int lastRow;
    /**
     * 从第几列到第几列
     */
    private int firstCol;
    private int lastCol;
    /**
     * 联动错误信息
     */
    private String errorMsg;
    /**
     * 联动cell开始行数
     */
    private int startRow;
    /**
     * 设置cell要控制的列数  例如  F列控制第7列的cell   offset:"F",colNum:7
     */
    private List<LevelInfo> levelInfoList;
}
