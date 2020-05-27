package com.fans.threadpool.eventBean;

import lombok.*;

import java.util.Date;

/**
 * className: PayBean
 *
 * @author k
 * @version 1.0
 * @description
 * @date 2018-12-20 14:14
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PayBean {
    private Long orderNo;
    private Long productId;
    private String productName;
    private double price;
    private Date createTime;
}
