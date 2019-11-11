package com.fans.utils.excel.param;

import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.fans.utils.excel.handler.CustomCellWriteHandler;
import com.fans.utils.excel.handler.CustomSheetWriteHandler;
import com.fans.utils.excel.listener.ExcelListener;
import com.google.common.collect.Lists;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ReadExcelParam
 * @Description: 生成excel入参
 * @Author k
 * @Date 2019-11-11 17:17
 * @Version 1.0
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class WriteExcelParam<T> implements Serializable {

    private static final long serialVersionUID = 2722957148771440143L;

    /**
     * sheet名称
     */
    private String sheetName = "未定义";
    /**
     * 文件绝对路径
     */
    private String filePath;
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
    /**
     * 带模板的数据源
     */
    private List<T> modelData = Lists.newArrayList();
    /**
     * excel模板
     */
    private Class<T> model;

    /**
     * excel解析监听器  没定义则用默认解析器
     */
    private Class<? extends AnalysisEventListener> listener = ExcelListener.class;
    /**
     * 多sheet数据模板
     */
    private List<MultipleSheetProperty<T>> multipleSheetProperties = Lists.newArrayList();
    /**
     * 自定义单元格拦截器 没定义则用默认拦截器
     */
    private Class<? extends CellWriteHandler> cellWriteHandler = CustomCellWriteHandler.class;
    /**
     * 自定义sheet拦截器 没定义则用默认拦截器
     */
    private Class<? extends SheetWriteHandler> sheetWriteHandler = CustomSheetWriteHandler.class;
}
