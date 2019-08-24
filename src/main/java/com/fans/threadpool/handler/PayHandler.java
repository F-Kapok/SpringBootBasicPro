package com.fans.threadpool.handler;

import com.fans.threadpool.basic.BaseEventHandler;
import com.fans.threadpool.basic.EventQueue;
import com.fans.threadpool.eventBean.PayBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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
        ThreadPoolExecutor threadPoolExecutor = EventQueue.getThreadPoolExecutor(event);
        System.out.println(threadPoolExecutor.getThreadFactory());
        PayHandlerKitchen payHandlerKitchen = new PayHandlerKitchen();
        PayHandlerFood payHandlerFood = new PayHandlerFood();
//        Future
        Future<Boolean> kitchenFuture = threadPoolExecutor.submit(payHandlerKitchen);
        Future<Boolean> foodFuture = threadPoolExecutor.submit(payHandlerFood);

        try {
            Boolean haveKitchen = kitchenFuture.get();
            Boolean haveFood = foodFuture.get();
            if (haveKitchen && haveFood) {
                PayHandlerCook payHandlerCook = new PayHandlerCook();
                threadPoolExecutor.submit(payHandlerCook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                Thread.sleep(1000);
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
