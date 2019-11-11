package com.fans.utils.excel.param;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MultipleSheetProperty
 * @Description: 多sheet数据模板
 * @Author k
 * @Date 2019-11-11 15:39
 * @Version 1.0
 **/
@Builder
@Data
public class MultipleSheetProperty<T> {

    private String sheetName;
    private List<T> data;
}
