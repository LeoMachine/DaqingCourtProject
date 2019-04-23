package com.boju.daqingcourt.bean;


import org.greenrobot.eventbus.Subscribe;

/**
 *
 * 从发布者那里得到eventbus传送过来的数据
 *
 * 加上@Subscribe以防报错：its super classes have no public methods with the @Subscribe annotation
 *
 */
public class MessageEvent<T> {
    private T message;
    private T message2;
    public MessageEvent(T message){
        this.message=message;
    }
    public MessageEvent(T message,T message2){
        this.message=message;
        this.message2=message2;
    }
    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public T getMessage2() {
        return message2;
    }

    public void setMessage2(T message2) {
        this.message2 = message2;
    }

}
