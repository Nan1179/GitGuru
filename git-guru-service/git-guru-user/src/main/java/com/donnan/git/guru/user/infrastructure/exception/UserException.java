package com.donnan.git.guru.user.infrastructure.exception;


import com.donnan.git.guru.base.exception.BizException;
import com.donnan.git.guru.base.exception.ErrorCode;

/**
 * 用户异常
 *
 * @author Donnan
 */
public class UserException extends BizException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public UserException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public UserException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }

}
