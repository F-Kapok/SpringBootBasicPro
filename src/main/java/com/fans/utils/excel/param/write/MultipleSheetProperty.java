package com.fans.utils.excel.param.write;

import com.fans.utils.excel.base.CellBaseHandler;
import com.fans.utils.excel.base.SheetBaseHandler;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * className: MultipleSheetProperty
 *
 * @author k
 * @version 1.0
 * @description 多sheet数据模板
 * @date 2020-05-23 12:40
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MultipleSheetProperty<T> implements Serializable {

    private static final long serialVersionUID = -20200523124022L;
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
