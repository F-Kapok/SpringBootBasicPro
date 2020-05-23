package com.fans.utils.excel.param.write;

import com.fans.utils.excel.base.CellBaseHandler;
import com.fans.utils.excel.base.SheetBaseHandler;
import com.google.common.collect.Lists;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * className: WriteFreedomParam
 *
 * @author k
 * @version 1.0
 * @description 自由模板生成excel入参
 * @date 2020-05-22 23:07
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class WriteFreedomParam implements Serializable {

    private static final long serialVersionUID = -20200522230755L;
    /**
     * 文件绝对路径
     */
    private String filePath;
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
    private List<List<String>> data = Lists.newArrayList();

}
