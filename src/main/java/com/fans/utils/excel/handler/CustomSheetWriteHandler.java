package com.fans.utils.excel.handler;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.fans.utils.excel.base.SheetBaseHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName CustomSheetWriteHandler
 * @Description: 自定义sheet拦截器
 * @Author k
 * @Date 2019-11-11 15:35
 * @Version 1.0
 **/
@Slf4j
public class CustomSheetWriteHandler extends SheetBaseHandler {
    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        log.info("第{}个Sheet写入成功。", writeSheetHolder.getSheetNo());
        // 区间设置 第三行第一列和第二列的数据。
        setPullDown(writeSheetHolder.getSheet(), 2, 2, 0, 1, super.trueOrFalse);
    }
}
