package com.boju.daqingcourt.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/16.
 * 原告信息
 */

public class YuangaoInfo implements Serializable{

    /**
     * id : 1
     * user_id : 2
     * categroy_id : 0
     * fayuan : 1
     * yiju : 1
     * content : null
     * remark : null
     * images :
     * falv :
     * add_ime : 2018-07-16 02:48:38
     * status : 0
     * number : null
     */

    private int id;
    private int user_id;
    private int categroy_id;
    private String fayuan;
    private String yiju;
    private String content;
    private String remark;
    private String images;
    private String falv;
    private String add_ime;
    private int status;
    private String number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategroy_id() {
        return categroy_id;
    }

    public void setCategroy_id(int categroy_id) {
        this.categroy_id = categroy_id;
    }

    public String getFayuan() {
        return fayuan;
    }

    public void setFayuan(String fayuan) {
        this.fayuan = fayuan;
    }

    public String getYiju() {
        return yiju;
    }

    public void setYiju(String yiju) {
        this.yiju = yiju;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getFalv() {
        return falv;
    }

    public void setFalv(String falv) {
        this.falv = falv;
    }

    public String getAdd_ime() {
        return add_ime;
    }

    public void setAdd_ime(String add_ime) {
        this.add_ime = add_ime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
