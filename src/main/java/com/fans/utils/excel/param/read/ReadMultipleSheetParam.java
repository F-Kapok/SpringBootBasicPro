package com.fans.utils.excel.param.read;

import com.fans.utils.excel.base.AbstractExcelBaseListener;
import com.google.common.collect.ImmutableMap;
import lombok.*;

import java.io.Serializable;

/**
 * className: ReadMultipleSheetParam
 *
 * @author k
 * @version 1.0
 * @description 读取多sheet多模板入参
 * @date 2020-05-19 00:59
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ReadMultipleSheetParam implements Serializable {

    private static final long serialVersionUID = -20200519005955L;
    /**
     * key:sheet页码，value:第几行开始读取数据，默认为0, 表示从第一行开始读取
     */
    private ImmutableMap<Integer, Integer> sheetNoAndHeadRowNumber;
    /**
     * 文件绝对路径
     */
    private String filePath;
    /**
     * key:sheet页码，value:excel模板
     */
    private ImmutableMap<Integer, Class<?>> sheetNoAndModel;
    /**
     * key:sheet页码，value:excel监听器
     */
    private ImmutableMap<Integer, AbstractExcelBaseListener<?>> sheetNoAndListener;
}
