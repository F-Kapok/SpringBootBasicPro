package com.fans.config;

import com.fans.annotation.Encrypt;
import com.fans.utils.EncryptUtils;
import com.fans.utils.JsonUtils;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ClassName MyResponseBodyAdvice
 * @Description: 常用于返回数据加密 @ResponseBody
 * @Author fan
 * @Date 2019-03-22 13:22
 * @Version 1.0
 **/
@ControllerAdvice(basePackages = {"com.fans.controller"})
@Slf4j
public class MyResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //TODO 自定义返回数据处理
        //采用注解的方式判断
        boolean encode = false;
        //如果方法上有该注解
        if (returnType.getMethod().isAnnotationPresent(Encrypt.class)) {
            //获取注解配置的字段
            Encrypt encrypt = returnType.getMethodAnnotation(Encrypt.class);
            //是否加密
            assert encrypt != null;
            encode = encrypt.encode();
        }
        if (encode) {
            log.info("对方法method :【" + returnType.getMethod().getName() + "】返回数据进行加密");
            try {
                String result = JsonUtils.obj2String(body);
                return EncryptUtils.md5Encrypt(result, Charsets.UTF_8.name());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return body;
    }
}
