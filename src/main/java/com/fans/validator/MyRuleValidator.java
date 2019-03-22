package com.fans.validator;

import com.fans.annotation.MyRule;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName MyRuleValidator
 * @Description: 自定义字段规则校验
 * @Author fan
 * @Date 2019-03-22 13:42
 * @Version 1.0
 **/
public class MyRuleValidator implements ConstraintValidator<MyRule, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int length = 20;
        //自定义规则
        if (StringUtils.isBlank(value)) {
            //去除默认message
            context.disableDefaultConstraintViolation();
            //更改新的message
            context.buildConstraintViolationWithTemplate("不能为空")
                    .addConstraintViolation();
            return false;
        } else if (value.length() > length) {
            //去除默认message
            context.disableDefaultConstraintViolation();
            //更改新的message
            context.buildConstraintViolationWithTemplate("长度不能超过20")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
