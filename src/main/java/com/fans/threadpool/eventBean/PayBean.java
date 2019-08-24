package com.fans.threadpool.eventBean;

import lombok.*;

import java.util.Date;

/**
 * @ClassName PayBean
 * @Description:
 * @Author k
 * @Date 2019-08-18 23:11
 * @Version 1.0
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
