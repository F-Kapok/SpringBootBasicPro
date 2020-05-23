package com.fans.utils.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * className: CustomStringDateConverter
 *
 * @author k
 * @version 1.0
 * @description 字符串时间转化
 * @date 2020-05-18 23:41
 **/
@Slf4j
public class CustomStringDateConverter implements Converter<Date> {
    @Override
    public Class<Date> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 读的时候调用
     *
     * @param cellData            单元格数据
     * @param contentProperty     上下文
     * @param globalConfiguration 全局配置
     * @return 日期
     */
    @Override
    public Date convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        //数据表中的时间 date类型
        try {
            String dateStr = cellData.getStringValue();
            if (StringUtils.isNotBlank(dateStr) && !"第八列".equals(cellData.getStringValue())) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy年MM月dd日 HH时mm分ss秒");
                DateTime dateTime = DateTime.parse(dateStr, dateTimeFormatter);
                return dateTime.toDate();
            }
        } catch (Exception e) {
            log.error("-->转化器读取出错:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 这里写的时候调用
     *
     * @param value               值
     * @param contentProperty     上下文
     * @param globalConfiguration 全局配置
     * @return 写入的值
     */
    @Override
    public CellData<String> convertToExcelData(Date value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //excel中的时间格式  2020年05月05日
        try {
            DateTime dateTime = new DateTime(value);
            return new CellData<>(dateTime.toString("yyyy年MM月dd日 HH时mm分ss秒"));
        } catch (Exception e) {
            log.error("-->转化器写入出错:{}", e.getMessage(), e);
        }
        return new CellData<>(StringUtils.EMPTY);
    }


}
