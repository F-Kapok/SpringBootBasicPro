package com.fans.utils.excel.param.other;

import lombok.*;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.Serializable;

/**
 * className: CellPullDownParam
 *
 * @author k
 * @version 1.0
 * @description cell表格下拉设置入参
 * @date 2020-07-01 11:08
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CellPullDownParam implements Serializable {

    private static final long serialVersionUID = -20200701110806L;

    /**
     * 要操作的sheet
     */
    private Sheet currentSheet;

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
     * 下拉列表
     */
    private String[] context;

}
