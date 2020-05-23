package com.fans.utils.excel.base;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

/**
 * className: CellBaseHandler
 *
 * @author k
 * @version 1.0
 * @description 单元格处理器
 * @date 2020-05-22 23:26
 **/
public class CellBaseHandler implements CellWriteHandler {


    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    public void setHeadStyle(Cell cell, Boolean isHead, List<Integer> mainCol) {
        if (isHead) {
            CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
            Font font = cell.getSheet().getWorkbook().createFont();
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setWrapText(true);
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            if (mainCol.contains(cell.getColumnIndex())) {
                font.setColor(IndexedColors.RED.index);
            }
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
        }
    }

    public void setHeadUrl(Cell cell, Boolean isHead, String url, int column) {
        if (isHead && cell.getColumnIndex() == column) {
            CreationHelper createHelper = cell.getSheet().getWorkbook().getCreationHelper();
            Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.URL);
            hyperlink.setAddress(url);
            cell.setHyperlink(hyperlink);
        }
    }


}
