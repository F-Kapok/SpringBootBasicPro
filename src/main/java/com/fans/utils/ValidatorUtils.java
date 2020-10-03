package com.fans.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * className: ValidatorUtils
 *
 * @author k
 * @version 1.0
 * @description hibernate-validator数据校验工具类
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 * @date 2018-11-06 12:12
 **/
public class ValidatorUtils {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    private static <T> Map<String, Object> validate(T t, Class<?>... groups) {
        Validator validator = VALIDATOR_FACTORY.getValidator();
        Set<ConstraintViolation<Object>> validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            Map<String, Object> errors = Maps.newHashMap();
            for (ConstraintViolation<?> violation : validateResult) {
                String message = violation.getMessage();
                Object invalidValue = violation.getInvalidValue();
                if (invalidValue != null && StringUtils.isNotBlank(invalidValue.toString())) {
                    message = "【" + invalidValue.toString() + "】" + message;
                }
                errors.put(violation.getPropertyPath().toString(), message);
            }
            return errors;
        }
    }

    /**
     * description:  校验集合类
     *
     * @param collection 要校验的集合
     * @param groups     校验组
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author k
     * @date 2018/11/06 11:45
     **/
    private static Map<String, Object> validateCollection(Collection<?> collection, Class<?>... groups) {
        // 判断集合是否为空
        Preconditions.checkNotNull(collection);
        Map<String, Object> errors = Maps.newHashMap();
        int i = 0;
        for (Object object : collection) {
            errors.put("集合中第" + (i + 1) + "个元素" + StringUtils.EMPTY, validate(object, groups));
            i++;
        }
        return errors;
    }

    /**
     * description: 字段校验检测
     *
     * @param param  校验参数
     * @param groups 校验组
     * @author k
     * @date 2018/11/06 12:14
     **/
    public static void check(Object param, Class<?>... groups) {
        Map<String, Object> map;
        if (param instanceof Collection<?>) {
            map = validateCollection((Collection<?>) param, groups);
        } else {
            map = ValidatorUtils.validate(param, groups);
        }
        if (MapUtils.isNotEmpty(map)) {
            throw new RuntimeException(map.toString());
        }
    }

    /**
     * description: 获取对象验证错误信息
     *
     * @param bindingResult 绑定对象结果信息
     * @return com.alibaba.fastjson.JSONObject 错误对象
     * @author k
     * @date 2019/11/19 21:21
     **/
    public static JSONObject getErrors(BindingResult bindingResult) {
        JSONObject jsonObject = new JSONObject();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> {
                fieldError.getField();
                fieldError.getDefaultMessage();
                jsonObject.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
        }
        return jsonObject;
    }
}
