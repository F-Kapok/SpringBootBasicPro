package com.fans.utils.excel.param.config;

import lombok.*;

import java.io.Serializable;

/**
 * className: LevelInfo
 *
 * @author k
 * @version 1.0
 * @description 联动设置信息
 * @date 2020-05-22 23:29
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LevelInfo implements Serializable {

    private static final long serialVersionUID = -20200522232926L;
    /**
     * 联动列 例：F
     */
    private String offset;
    /**
     * 被联动列 例：7
     */
    private int colNum;
}
