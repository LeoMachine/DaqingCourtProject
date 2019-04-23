package com.boju.daqingcourt.retrofitclient;


/**
 * 网络返回基类 支持泛型
 * Created by Tamic on 2016-06-06.
 */
public class HttpResponse<T> {
    private String msg;
    private int code;
    public T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
