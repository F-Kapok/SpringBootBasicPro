package com.fans.utils.excel.handler;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.fans.utils.excel.base.SheetBaseHandler;
import com.fans.utils.excel.param.other.LinkageParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

/**
 * className: CustomSheetWriteHandler
 *
 * @author k
 * @version 1.0
 * @description 自定义sheet拦截器
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class CustomSheetWriteHandler extends SheetBaseHandler {
    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        log.info("第{}个Sheet写入成功。", writeSheetHolder.getSheetNo());
        // 区间设置 第三行第一列和第二列的数据。
        setPullDown(writeSheetHolder.getSheet(), 2, 2, 0, 1, super.trueOrFalse);
        //增加二级联动
        twoLinked(writeSheetHolder);
        //增加三级联动
        threeLinked(writeSheetHolder);
    }

    private void threeLinked(WriteSheetHolder writeSheetHolder) {
        String[] parent = Stream.of("广东省", "北京", "海南省").toArray(String[]::new);
        String[] guangDong = Stream.of("广州市", "佛山市").toArray(String[]::new);
        String[] beiJing = Stream.of("海淀区", "廊坊市").toArray(String[]::new);
        String[] haiNan = Stream.of("三亚市", "海口市").toArray(String[]::new);

        String[] guangZhougZone = Stream.of("海珠区", "越秀区", "天河区").toArray(String[]::new);
        String[] foshanZone = Stream.of("顺德区", "南海区", "三水区").toArray(String[]::new);

        String[] langfangZone = Stream.of("安次区", "广阳区").toArray(String[]::new);

        String[] sanyaZone = Stream.of("海棠区", "天涯区", "崖州区").toArray(String[]::new);
        String[] haikouZone = Stream.of("秀英区", "美兰区", "琼山区").toArray(String[]::new);
        LinkedHashMap<String, String[]> exceptLast = Maps.newLinkedHashMap();
        for (String parentKey : parent) {
            if ("广东省".equals(parentKey)) {
                exceptLast.put(parentKey, guangDong);
            } else if ("北京".equals(parentKey)) {
                exceptLast.put(parentKey, beiJing);
            } else if ("海南省".equals(parentKey)) {
                exceptLast.put(parentKey, haiNan);
            }
            for (String guangdongKey : guangDong) {
                if ("广州市".equals(guangdongKey)) {
                    exceptLast.put(guangdongKey, guangZhougZone);
                } else if ("佛山市".equals(guangdongKey)) {
                    exceptLast.put(guangdongKey, foshanZone);
                }
            }
            for (String beijingKey : beiJing) {
                if ("海淀区".equals(beijingKey)) {
                    exceptLast.put(beijingKey, new String[0]);
                }
                if ("廊坊市".equals(beijingKey)) {
                    exceptLast.put(beijingKey, langfangZone);
                }
            }
            for (String hainanKey : haiNan) {
                if ("三亚市".equals(hainanKey)) {
                    exceptLast.put(hainanKey, sanyaZone);
                } else if ("海口市".equals(hainanKey)) {
                    exceptLast.put(hainanKey, haikouZone);
                }
            }
        }
        setLinked(LinkageParam.builder()
                .parent(parent)
                .hideSheetName("省市区")
                .firstRow(1)
                .exceptLast(exceptLast)
                .errorMsg("请选择正确的省市区类型")
                .currentSheet(writeSheetHolder.getSheet())
                .lastRow(1000)
                .letter(Lists.newArrayList("G", "H"))
                .build());
    }

    private void twoLinked(WriteSheetHolder writeSheetHolder) {
        String[] parent = Stream.of("电商推广", "金融", "快消").toArray(String[]::new);
        String[] dianshang = Stream.of("电商平台", "电商活动").toArray(String[]::new);
        String[] jinrong = Stream.of("金融").toArray(String[]::new);
        String[] kuaixiao = Stream.of("食品饮料", "餐饮", "美妆", "保健和药品", "母婴", "日用", "服饰箱包").toArray(String[]::new);
        LinkedHashMap<String, String[]> exceptLast = Maps.newLinkedHashMap();
        for (String key : parent) {
            if ("电商推广".equals(key)) {
                exceptLast.put(key, dianshang);
            } else if ("金融".equals(key)) {
                exceptLast.put(key, jinrong);
            } else if ("快消".equals(key)) {
                exceptLast.put(key, kuaixiao);
            }
        }
        setLinked(LinkageParam.builder()
                .currentSheet(writeSheetHolder.getSheet())
                .errorMsg("请选择正确的广告类型")
                .exceptLast(exceptLast)
                .firstRow(1)
                .lastRow(10)
                .hideSheetName("广告联动")
                .parent(parent)
                .letter(Lists.newArrayList("D"))
                .build());
    }
}
