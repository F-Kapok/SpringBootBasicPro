package com.fans.threadpool.handler;

import com.fans.threadpool.basic.BaseEventHandler;
import com.fans.threadpool.basic.EventQueue;
import com.fans.threadpool.eventBean.PayBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

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
    public int getCorePoolSize() {
        return 10;
    }

    @Override
    public BlockingQueue<Runnable> getWorkQueue() {
        return new ArrayBlockingQueue(10);
    }

    @Override
    public void execute(PayBean event) {
        PayHandlerKitchen payHandlerKitchen = new PayHandlerKitchen();
        PayHandlerFood payHandlerFood = new PayHandlerFood();
        PayHandlerCook payHandlerCook = new PayHandlerCook();
        EventQueue.gatherSubmit(event, 1, TimeUnit.SECONDS, payHandlerCook, payHandlerKitchen, payHandlerFood);
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

    private static class PayHandlerKitchen implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                System.out.println("买厨具");
                Thread.sleep(3000);
                System.out.println("买好厨具");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private static class PayHandlerFood implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                System.out.println("买食材");
                Thread.sleep(2000);
                System.out.println("买好食材");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private static class PayHandlerCook implements Callable<Boolean> {
        @Override
        public Boolean call() {
            try {
                System.out.println("做饭");
                Thread.sleep(5000);
                System.out.println("做好饭");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
