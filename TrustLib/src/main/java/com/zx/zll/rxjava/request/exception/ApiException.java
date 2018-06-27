package com.zx.zll.rxjava.request.exception;

/**
 * 请求错误处理类
 * Created by Moon on 2018/6/22.
 */

public class ApiException extends Exception{
    private int code;
    private String msg;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
