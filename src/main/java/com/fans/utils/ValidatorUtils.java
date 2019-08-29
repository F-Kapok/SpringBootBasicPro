package com.fans.utils;

import com.fans.exception.ParamException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @ClassName ValidatorUtils
 * @Description: hibernate-validator数据校验工具类
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 * @Author k
 * @Date 2018-11-06 12:12
 * @Version 1.0
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
     * @Description: 校验集合类
     * @Param: [collection, groups]
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @Author: fan
     * @Date: 2018/11/06 11:45
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
     * @Description: 校验整合
     * @Param: [first, objects, groups]
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @Author: fan
     * @Date: 2018/11/06 12:14
     **/
    public static Map<String, String> validateObject(Object first, Object[] objects, Class... groups) {
        if (objects != null && objects.length > 0) {
            return validateCollection(Lists.asList(first, objects));
        } else {
            return validate(first, groups);
        }
    }

    /**
     * @Description: 字段校验检测
     * @Param: [param, groups]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/06 12:14
     **/
    public static void check(Object param, Class... groups) {
        Map<String, String> map = ValidatorUtils.validateObject(param, groups);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }
}
