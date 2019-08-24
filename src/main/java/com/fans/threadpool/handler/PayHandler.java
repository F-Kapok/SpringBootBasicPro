package com.fans.threadpool.handler;

import com.fans.threadpool.basic.BaseEventHandler;
import com.fans.threadpool.eventBean.PayBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName PayHandler
 * @Description:
 * @Author k
 * @Date 2019-08-18 23:15
 * @Version 1.0
 **/
@Component(value = "payHandler")
@Slf4j
public class PayHandler extends BaseEventHandler<PayBean> {

    @Override
    public void execute(PayBean event) {
        log.info("--> 扣款执行开始》》》》》》》");
        log.info("--> 订单号：{}", event.getOrderNo());
        log.info("--> 商品名称：{}", event.getProductName());
        log.info("--> 总价为：{}", event.getPrice());
        log.info("--> 创建时间：{}", event.getCreateTime());
        log.info("--> 扣款执行=============================");
        log.info("--> 扣款 success！");
        log.info("--> 扣库存执行=============================");
        log.info("--> 扣库存 success！");
    }

    @Override
    public String getDescription() {
        return "支付执行器,支付后执行逻辑 例如扣库存";
    }
}
