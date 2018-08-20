package com.clvspk.photo.response;

import com.clvspk.photo.exception.BusinessException;

/**
 * 响应实体
 *
 * @author Mr.C
 * @date 2018-6-26 10.00
 */
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result<Object> success(Object data) {
        return new Result<>(Const.SUCCESS_CODE, Const.SUCCESS_DEFAULT_MSG, data);
    }

    public static Result failMsg(String msg) {
        return new Result<>(Const.FAIL_CODE, msg);
    }

    public static void fail(String msg) {
        throw new BusinessException(Const.FAIL_CODE, msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
