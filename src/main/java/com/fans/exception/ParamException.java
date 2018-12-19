package com.fans.exception;

/**
 * @ClassName ParamException
 * @Description: TODO 参数异常处理
 * @Author fan
 * @Date 2018-11-20 09:46
 * @Version 1.0
 **/
public class ParamException extends RuntimeException {
    public ParamException() {
        super();
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

    protected ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
