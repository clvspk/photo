package com.clvspk.photo.exception;

/**
 * 业务异常
 * Created on 2017/8/22/022.
 *
 * @author MrC
 */
public class BusinessException extends RuntimeException {

    private int code;

    protected BusinessException() {
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
