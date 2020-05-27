package com.fans.utils.excel.handler;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.fans.utils.excel.base.CellBaseHandler;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * className: CustomCellWriteHandler
 *
 * @author k
 * @version 1.0
 * @description 自定义单元格拦截器
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class CustomCellWriteHandler extends CellBaseHandler {
    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
        super.beforeCellCreate(writeSheetHolder, writeTableHolder, row, head, columnIndex, relativeRowIndex, isHead);
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        //给指定head单元格增加url
        setHeadUrl(cell, isHead, "https://github.com/alibaba/easyexcel", 0);
        //给指定head单元格增加特殊颜色（red）
        setHeadStyle(cell, isHead, Lists.newArrayList(1, 3));
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        log.info("--> 第{}行，第{}列写入【{}】完成。", cell.getRowIndex(), cell.getColumnIndex(), cell.getStringCellValue());
        super.afterCellDispose(writeSheetHolder, writeTableHolder, cellDataList, cell, head, relativeRowIndex, isHead);
    }
}
