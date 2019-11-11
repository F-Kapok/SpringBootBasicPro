package com.fans.utils.excel.param;

import com.alibaba.excel.event.AnalysisEventListener;
import com.fans.utils.excel.listener.ExcelListener;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName ReadExcelParam
 * @Description: 读取excel入参
 * @Author k
 * @Date 2019-11-11 17:17
 * @Version 1.0
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ReadExcelParam<T> implements Serializable {

    private static final long serialVersionUID = 7238606235812967896L;
    /**
     * sheet页码，默认为 0
     */
    private int sheetNo = 0;
    /**
     * 第几行开始读取数据，默认为0, 表示从第一行开始读取
     */
    private int headRowNumber = 0;
    /**
     * 文件绝对路径
     */
    private String filePath;
    /**
     * excel模板
     */
    private Class<T> model;
    /**
     * excel解析监听器  没定义则用默认解析器
     */
    private Class<? extends AnalysisEventListener> listener = ExcelListener.class;
}
