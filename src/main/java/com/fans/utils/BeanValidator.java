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
 * @ClassName BeanValidator
 * @Description: hibernate-validator数据校验工具类
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 * @Author fan
 * @Date 2018-11-06 12:12
 * @Version 1.0
 **/
public class BeanValidator {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap<String, String> errors = Maps.newLinkedHashMap();
            for (Object o : validateResult) {
                ConstraintViolation violation = (ConstraintViolation) o;
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    /**
     * @Description: 校验集合类
     * @Param: [collection]
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @Author: fan
     * @Date: 2018/11/06 11:45
     **/
    private static Map<String, String> validateCollection(Collection<?> collection) {
        // 判断集合是否为空
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

    /**
     * @Description: 校验整合
     * @Param: [first, objects]
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @Author: fan
     * @Date: 2018/11/06 12:14
     **/
    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateCollection(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }

    /**
     * @Description: 字段校验检测
     * @Param: [param]
     * @return: void
     * @Author: fan
     * @Date: 2018/11/06 12:14
     **/
    public static void check(Object param) {
        Map<String, String> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }
}
