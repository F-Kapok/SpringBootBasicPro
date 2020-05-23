package com.fans.utils.excel.param.write;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * className: WriteFreedomMultipleParam
 *
 * @author k
 * @version 1.0
 * @description 多sheet自由模板入参
 * @date 2020-05-23 13:33
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class WriteFreedomMultipleParam implements Serializable {

    private static final long serialVersionUID = -20200523133301L;

    private String filePath;

    private List<MultipleFreedomSheetProperty> multipleFreedomSheetProperties;
}
