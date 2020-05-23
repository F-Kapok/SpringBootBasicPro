package com.fans.utils.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.fans.utils.excel.base.AbstractExcelBaseListener;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * className: ExcelListener
 *
 * @author k
 * @version 1.0
 * @description 监听器
 * @date 2020-05-22 23:19
 **/
@Slf4j
@Setter
@Getter
public class ExcelListener<T> extends AbstractExcelBaseListener<T> {

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

    @Override
    public boolean checkData(int rowIndex, T data) {
        return false;
    }

}
