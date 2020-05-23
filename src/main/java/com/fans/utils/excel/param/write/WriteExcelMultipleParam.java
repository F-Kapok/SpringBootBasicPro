package com.fans.utils.excel.param.write;

import com.google.common.collect.Lists;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * className: WriteExcelMultipleParam
 *
 * @author k
 * @version 1.0
 * @description 生成excel多sheet入参-有模板
 * @date 2020-05-23 12:36
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class WriteExcelMultipleParam implements Serializable {

    private static final long serialVersionUID = -20200523123644L;
    /**
     * 文件绝对路径
     */
    private String filePath;
    /**
     * 多sheet数据模板
     */
    private List<MultipleSheetProperty<?>> multipleSheetProperties = Lists.newArrayList();

}
