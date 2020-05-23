package com.fans.utils.excel.param.read;

import com.fans.utils.excel.base.AbstractExcelBaseListener;
import lombok.*;

import java.io.Serializable;

/**
 * className: ReadExcelParam
 *
 * @author k
 * @version 1.0
 * @description 单模板单sheet读取通用入参
 * @date 2020-05-18 23:05
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ReadExcelParam<T> implements Serializable {

    private static final long serialVersionUID = -20200518230516L;
    /**
     * sheet页码，默认为 0
     */
    private int sheetNo;
    /**
     * 第几行开始读取数据，默认为0, 表示从第一行开始读取
     */
    private int headRowNumber;
    /**
     * 文件绝对路径
     */
    private String filePath;
    /**
     * excel模板
     */
    private Class<T> model;
    /**
     * excel监听器
     */
    private AbstractExcelBaseListener<T> listener;

}
