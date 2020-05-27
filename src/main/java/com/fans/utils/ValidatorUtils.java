package com.fans.utils;

import com.alibaba.fastjson.JSONObject;
import com.fans.exception.ParamException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
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
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap<String, String> errors = Maps.newLinkedHashMap();
            for (ConstraintViolation violation : validateResult) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    /**
     * description: 校验集合类
     *
     * @param collection 要校验的集合
     * @param groups     校验组
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author k
     * @date 2018/11/06 11:45
     **/
    private static Map<String, String> validateCollection(Collection<?> collection, Class... groups) {
        // 判断集合是否为空
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map<String, String> errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, groups);
        } while (errors.isEmpty());
        return errors;
    }

    /**
     * description: 校验整合
     *
     * @param first   校验的对象
     * @param objects 校验对象数组
     * @param groups  校验组
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author k
     * @date 2018/11/06 12:14
     **/
    public static Map<String, String> validateObject(Object first, Object[] objects, Class... groups) {
        if (objects != null && objects.length > 0) {
            return validateCollection(Lists.asList(first, objects));
        } else {
            return validate(first, groups);
        }
    }

    /**
     * description: 字段校验检测
     *
     * @param param  校验参数
     * @param groups 校验组
     * @author k
     * @date 2018/11/06 12:14
     **/
    public static void check(Object param, Class... groups) {
        Map<String, String> map = ValidatorUtils.validateObject(param, groups);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
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
