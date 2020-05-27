package com.fans.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * className: ErrorHandler
 *
 * @author k
 * @version 1.0
 * @description 错误页面拦截器
 * @date 2018-12-20 14:14
 **/
@ControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ModelAndView error500(HttpServletRequest request, Exception e) {
        log.error("--> " + e.getMessage(), e);
        log.error("{} encounter 500", request.getRequestURL());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error/500");
        modelAndView.addObject("msg", "错误信息：" + e.getMessage());
        return modelAndView;
    }
}
