package com.fans.utils.excel.param.write;

import com.fans.utils.excel.base.CellBaseHandler;
import com.fans.utils.excel.base.SheetBaseHandler;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * className: WriteExcelParam
 *
 * @author k
 * @version 1.0
 * @description 生成excel入参
 * @date 2020-05-22 22:58
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class WriteExcelParam<T> implements Serializable {

    private static final long serialVersionUID = -20200522225816L;
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
     * 带模板的数据源
     */
    private List<T> modelData;
    /**
     * excel模板
     */
    private Class<T> model;
}
