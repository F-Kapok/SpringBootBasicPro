package com.fans.utils.excel.param.write;

import com.fans.utils.excel.base.CellBaseHandler;
import com.fans.utils.excel.base.SheetBaseHandler;
import com.google.common.collect.Lists;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * className: MultipleFreedomSheetProperty
 *
 * @author k
 * @version 1.0
 * @description 多sheet自由模板
 * @date 2020-05-23 13:29
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MultipleFreedomSheetProperty implements Serializable {

    private static final long serialVersionUID = -20200523132952L;
    /**
     * sheet名称
     */
    private String sheetName;
    /**
     * 自动列宽
     */
    private Boolean autoWidth;
    /**
     * cell处理器
     */
    private CellBaseHandler cellBaseHandler;
    /**
     * sheet处理器
     */
    private SheetBaseHandler sheetBaseHandler;


    /**
     * 简单单行表头
     */
    private List<String> simpleHead = Lists.newArrayList();
    /**
     * 复杂多行表头
     */
    private List<List<String>> hardHead = Lists.newArrayList();
    /**
     * 自定义数据源
     */
    private List<List<?>> data = Lists.newArrayList();
}
