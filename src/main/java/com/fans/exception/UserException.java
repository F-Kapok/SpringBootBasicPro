package com.fans.exception;

/**
 * className: UserException
 *
 * @author k
 * @version 1.0
 * @description 用户异常处理
 * @date 2018-12-20 14:14
 **/
public class UserException extends RuntimeException {
    private static final long serialVersionUID = -7034897190745766939L;

    private Long id;

    public UserException(Long id) {
        super("此用户信息异常");
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    protected UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
