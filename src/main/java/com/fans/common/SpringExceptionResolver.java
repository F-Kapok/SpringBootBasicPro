package com.fans.common;

import com.fans.exception.ParamException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * @ClassName SpringExceptionResolver
 * @Description: TODO 错误异常处理
 * @Author fan
 * @Date 2018-12-20 10:57
 * @Version 1.0
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
