package com.fans.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @ClassName ControllerAspect
 * @Description: 控制层AOP
 * @Author fan
 * @Date 2018-12-20 14:14
 * @Version 1.0
 **/
@Aspect
@Component
@Slf4j
public class ControllerAspect {
    /**
     * 在什么时候起作用 在那些方法起作用 表达式
     * Before 执行之前
     * After 执行之后
     * AfterThrowing 执行之后 包括抛出异常
     * Around 包含之前的三种 常用
     */
    @Around("execution(* com.fans.controller.HelloController.*(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("********** controller aspect start **********");
        Object[] objects = proceedingJoinPoint.getArgs();
        for (Object arg : objects) {
            log.info("--> arg is {}", arg);
        }
        Object object = proceedingJoinPoint.proceed();
        log.info("********** controller aspect stop **********");
        return object;
    }
}
