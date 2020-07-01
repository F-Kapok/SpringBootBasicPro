package com.fans.utils.excel.base;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.fans.utils.excel.param.other.CellPullDownParam;
import com.fans.utils.excel.param.other.LinkageParam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

/**
 * className: SheetBaseHandler
 *
 * @author k
 * @version 1.0
 * @description sheet处理器
 * @date 2020-05-22 23:27
 **/
public class SheetBaseHandler implements SheetWriteHandler {

    public final String[] trueOrFalse = Stream.of("是", "否").toArray(String[]::new);

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    public void setPullDown(CellPullDownParam cellPullDownParam) {
        Sheet currentSheet = cellPullDownParam.getCurrentSheet();
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(cellPullDownParam.getFirstRow(), cellPullDownParam.getLastRow(), cellPullDownParam.getFirstCol(), cellPullDownParam.getLastCol());
        DataValidationHelper helper = currentSheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(cellPullDownParam.getContext());
        DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
        dataValidation.createErrorBox("error", cellPullDownParam.getErrorMsg());
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        currentSheet.addValidationData(dataValidation);
    }


    public void setLinked(LinkageParam linkageParam) {
        Workbook workbook = linkageParam.getCurrentSheet().getWorkbook();
        Sheet hide = workbook.createSheet(linkageParam.getHideSheetName());
        workbook.setSheetHidden(workbook.getSheetIndex(hide), false);
        int rowId = 0;
        Row adRow = hide.createRow(rowId++);
        adRow.createCell(0).setCellValue(linkageParam.getHideSheetName());
        String[] parent = linkageParam.getParent();
        for (int i = 0; i < parent.length; i++) {
            Cell provinceCell = adRow.createCell(i + 1);
            provinceCell.setCellValue(parent[i]);
        }
        Map<String, String[]> exceptLast = linkageParam.getExceptLast();
        // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
        for (String key : exceptLast.keySet()) {
            String[] son = exceptLast.get(key);
            Row row = hide.createRow(rowId++);
            row.createCell(0).setCellValue(key);
            for (int j = 0; j < son.length; j++) {
                Cell cell = row.createCell(j + 1);
                cell.setCellValue(son[j]);
            }
            // 添加名称管理器
            String range = getRange(1, rowId, son.length);
            Name name = workbook.createName();
            //key不可重复
            name.setNameName(key);
            name.setRefersToFormula(linkageParam.getHideSheetName() + "!" + range);
        }
        //获取要控制的列
        List<String> letter = linkageParam.getLetter();
        //取出第一个当做开始列
        int startAndEndCol = letterToNumber(letter.get(0));
        Sheet currentSheet = linkageParam.getCurrentSheet();
        DataValidationHelper helper = currentSheet.getDataValidationHelper();
        DataValidationConstraint explicitListConstraint = helper.createExplicitListConstraint(parent);
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(linkageParam.getFirstRow(), linkageParam.getLastRow(), startAndEndCol - 1, startAndEndCol - 1);
        DataValidation dataValidation = helper.createValidation(explicitListConstraint, cellRangeAddressList);
        dataValidation.createErrorBox("error", linkageParam.getErrorMsg());
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        currentSheet.addValidationData(dataValidation);
        letter.forEach(offset -> {
            for (int i = linkageParam.getFirstRow() + 1; i < linkageParam.getLastRow(); i++) {
                setDataValidation(helper, offset, currentSheet, i, letterToNumber(offset) + 1);
            }
        });
    }

    /**
     * 计算formula
     *
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    private static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            // 26-51之间，包括边界（仅两次字母表计算）
            if ((colCount - 25) / 26 == 0 || colCount == 51) {
                // 边界值
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

    /**
     * 设置有效性
     *
     * @param offset 主影响单元格所在列，即此单元格由哪个单元格影响联动
     * @param sheet  sheet页
     * @param rowNum 要控制的行数
     * @param colNum 要控制的列数
     */
    private static void setDataValidation(DataValidationHelper helper, String offset, Sheet sheet, int rowNum, int colNum) {
        DataValidation dataValidationList = getDataValidationByFormula(
                "INDIRECT($" + offset + (rowNum) + ")", rowNum, colNum, helper);
        sheet.addValidationData(dataValidationList);
    }

    /**
     * 加载下拉列表内容
     *
     * @param formulaString      管理器key
     * @param naturalRowIndex    控制行
     * @param naturalColumnIndex 控制列
     * @param dvHelper           执行器
     * @return 执行结果
     */
    private static DataValidation getDataValidationByFormula(
            String formulaString, int naturalRowIndex, int naturalColumnIndex, DataValidationHelper dvHelper) {
        // 加载下拉列表内容
        // 举例：若formulaString = "INDIRECT($A$2)" 表示规则数据会从名称管理器中获取key与单元格 A2 值相同的数据，
        //如果A2是江苏省，那么此处就是江苏省下的市信息。
        DataValidationConstraint formulaListConstraint = dvHelper.createFormulaListConstraint(formulaString);
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        int firstRow = naturalRowIndex - 1;
        int lastRow = naturalRowIndex - 1;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                lastRow, firstCol, lastCol);
        // 数据有效性对象
        // 绑定
        DataValidation validation = dvHelper.createValidation(formulaListConstraint, regions);
        validation.setEmptyCellAllowed(false);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        // 设置输入信息提示信息
        validation.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        // 设置输入错误提示信息
        //data_validation_list.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        return validation;
    }


    /**
     * 将以字母表示的Excel列数转换成数字表示
     *
     * @param letter 以字母表示的列数，不能为空且只允许包含字母字符
     * @return 返回转换的数字，转换失败返回-1
     * @author k
     */
    private static int letterToNumber(String letter) {
        // 检查字符串是否为空
        if (letter == null || letter.isEmpty()) {
            return -1;
        }
        // 转为大写字符串
        String upperLetter = letter.toUpperCase();
        // 检查是否符合，不能包含非字母字符
        if (!upperLetter.matches("[A-Z]+")) {
            return -1;
        }
        // 存放结果数值
        long num = 0;
        long base = 1;
        // 从字符串尾部开始向头部转换
        for (int i = upperLetter.length() - 1; i >= 0; i--) {
            char ch = upperLetter.charAt(i);
            num += (ch - 'A' + 1) * base;
            base *= 26;
            // 防止内存溢出
            if (num > Integer.MAX_VALUE) {
                return -1;
            }
        }
        return (int) num;
    }

    /**
     * 将数字转换成以字母表示的Excel列数
     *
     * @param num 表示列数的数字
     * @return 返回转换的字母字符串，转换失败返回null
     * @author k
     */
    private static String numberToLetter(int num) {
        // 检测列数是否正确
        if (num <= 0) {
            return null;
        }
        StringBuilder letter = new StringBuilder();
        do {
            --num;
            // 取余
            int mod = num % 26;
            // 组装字符串
            letter.append((char) (mod + 'A'));
            // 计算剩下值
            num = (num - mod) / 26;
        } while (num > 0);
        // 返回反转后的字符串
        return letter.reverse().toString();
    }

    public static void main(String[] args) {
        System.out.println(letterToNumber("d"));
    }

    /**
     * 获取随机字符串
     *
     * @param length 随机字符串长度
     * @return 随机字符
     */
    private static String getStringRandom(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }
}
