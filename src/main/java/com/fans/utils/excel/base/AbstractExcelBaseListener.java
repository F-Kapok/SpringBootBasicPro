package com.fans.utils.excel.base;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * className: AbstractExcelBaseListener
 *
 * @author k
 * @version 1.0
 * @description 解析监听器，每解析一行会回调invoke()方法。整个excel解析结束会执行doAfterAllAnalysed()方法
 * @date 2018-12-20 14:14
 **/
@Slf4j
@Setter
@Getter
public abstract class AbstractExcelBaseListener<T> extends AnalysisEventListener<T> {


    private List<T> dataList = Lists.newArrayList();

    long startTime = System.currentTimeMillis();


    /**
     * 逐行解析
     *
     * @param data    当前行的数据
     * @param context excel工作簿信息
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("--> 当前行数：{}", context.readRowHolder().getRowIndex());
        if (data != null) {
            dataList.add(data);
        }
    }

    /**
     * 解析完所有数据后会调用该方法
     *
     * @param context excel工作簿信息
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //解析结束销毁不用的资源
        log.info("--> {} 条数据，开始存储数据库等等操作 !", dataList.size());
        log.info("--> 所有数据解析完成！用时{}ms", System.currentTimeMillis() - startTime);
    }

    /**
     * 校验数据
     */
    public abstract boolean checkData(int rowIndex, T data);
}
