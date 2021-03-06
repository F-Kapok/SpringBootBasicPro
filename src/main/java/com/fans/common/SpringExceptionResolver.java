package com.fans.common;

import com.fans.exception.ParamException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * className: SpringExceptionResolver
 *
 * @author k
 * @version 1.0
 * @description 错误异常处理
 * @date 2018-12-20 14:14
 **/
@ControllerAdvice
public class SpringExceptionResolver {
    @ExceptionHandler(ParamException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonData handlerUserException(ParamException exception) {
        exception.printStackTrace();
        return JsonData.fail("--> This have problem " + exception);
    }


}
